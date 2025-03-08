package com.example.student_recipe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.student_recipe.model.Recipe
import com.example.student_recipe.ui.components.RecipeCard
import com.example.student_recipe.repository.RecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RecipeListScreen(navController: NavController, repository: RecipeRepository) {
    var recipes by remember { mutableStateOf(emptyList<Recipe>()) }
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentPage by remember { mutableStateOf(1) }
    var endReached by remember { mutableStateOf(false) } // ✅ Empêcher le chargement infini

    val coroutineScope = rememberCoroutineScope()

    // ✅ Ajout d'un debounce pour éviter les appels excessifs à l'API
    LaunchedEffect(query) {
        delay(500) // Attend 500ms après la dernière saisie avant d'exécuter la requête
        if (query.isBlank()) {
            // ✅ Si la recherche est vide, charger toutes les recettes (API + Local)
            currentPage = 1
            endReached = false
            loadRecipes(repository, query, currentPage, onResult = {
                recipes = it
            }, onError = {
                errorMessage = "Erreur de chargement des recettes."
            })
        } else {
            // ✅ Si l'utilisateur tape un mot, faire uniquement une recherche locale
            val results = repository.searchRecipes(query)
            recipes = results
        }
    }

    suspend fun loadNextPage() {
        if (isLoading || endReached) return // ✅ Empêche les appels multiples si une requête est déjà en cours
        currentPage++
        loadRecipes(repository, query, currentPage, onResult = {
            if (it.isEmpty()) {
                endReached = true // ✅ Si la nouvelle page est vide, on arrête le chargement
            } else {
                recipes += it
            }
        }, onError = {

            errorMessage = "Erreur de chargement des recettes."
        })
    }

    // Interface utilisateur
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(query = query, onQueryChanged = { query = it })

        if (isLoading && recipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage ?: "Une erreur est survenue", color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(recipes) { recipe ->
                    RecipeCard(recipe = recipe, onClick = {
                        navController.navigate("details/${recipe.id}")
                    })
                }

                item {
                    if (!isLoading && !endReached) {
                        LaunchedEffect(Unit) {
                            coroutineScope.launch {
                                loadNextPage()
                            }
                        }
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

suspend fun loadRecipes(
    repository: RecipeRepository,
    query: String,
    page: Int,
    onResult: (List<Recipe>) -> Unit,
    onError: () -> Unit
) {
    try {
        val newRecipes = repository.getRecipes(query, page)
        onResult(newRecipes)
    } catch (e: Exception) {
        println(e)
        onError()
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = { newText -> onQueryChanged(newText) },
        label = { Text("Rechercher des recettes") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true // ✅ Évite les retours à la ligne inutiles
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeList() {
    val navController = rememberNavController()
}