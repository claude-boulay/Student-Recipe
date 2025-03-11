package com.example.student_recipe.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_recipe.model.Recipe
import com.example.student_recipe.ui.components.RecipeCard
import com.example.student_recipe.repository.RecipeRepository
import com.example.student_recipe.ui.theme.CustomDarkBrown
import kotlinx.coroutines.delay

// Barre de recherche pour filtrer les recettes
@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Student Recipes Book",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = query,
            onValueChange = { newText -> onQueryChanged(newText) },
            label = {
                Text(
                    text = "Search recipes",
                    color = CustomDarkBrown
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = CustomDarkBrown,
                focusedTextColor = CustomDarkBrown,
                unfocusedTextColor = CustomDarkBrown,
                focusedPlaceholderColor = CustomDarkBrown.copy(alpha = 0.6f),
                unfocusedPlaceholderColor = CustomDarkBrown.copy(alpha = 0.6f)
            )
        )
    }
}

@Composable
fun RecipeListScreen(navController: NavController, repository: RecipeRepository) {
    var recipes by remember { mutableStateOf(emptyList<Recipe>()) }
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentPage by remember { mutableStateOf(1) }
    var endReached by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Lancement de la requête lors de la modification de la requête de recherche
    LaunchedEffect(query) {
        delay(500) // Délai avant de lancer la recherche
        if (query.isBlank()) {
            currentPage = 1
            endReached = false
            loadRecipes(repository, query, currentPage, onResult = {
                recipes = it
            }, onError = {
                errorMessage = "Error loading recipes"
            })
        } else {
            val results = repository.searchRecipes(query)
            recipes = results
        }
    }

    // Fonction pour charger la prochaine page de recettes
    suspend fun loadNextPage() {
        if (isLoading || endReached || query.isNotBlank()) return
        isLoading = true
        currentPage=recipes.size/30+2
        loadRecipes(repository, query, currentPage, onResult = { newRecipes ->
            if (newRecipes.isEmpty()) {
                endReached = true // Fin des résultats
            } else {
                recipes += newRecipes

                // Incrémenter `currentPage` toutes les 30 recettes
                if (recipes.size % 30 == 0) {
                    currentPage=recipes.size/30+2
                }
            }
        }, onError = {
            errorMessage = "Error loading recipes."
        })
        isLoading = false
    }

    // Détection de la dernière item visible pour charger la page suivante
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                val totalItems = recipes.size
                if (lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItems - 5 && !isLoading) {
                    loadNextPage() // Chargement de la prochaine page
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barre de recherche pour filtrer les recettes
        SearchBar(query = query, onQueryChanged = { query = it })

        // Affichage d'un indicateur de chargement pendant que les recettes sont récupérées
        if (isLoading && recipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() // Indicataire de chargement
            }
        } else if (errorMessage != null) {
            // Affichage d'un message d'erreur si le chargement échoue
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage ?: "An error occurred", color = MaterialTheme.colorScheme.error)
            }
        } else {
            // Liste des recettes
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Affichage des recettes dans la liste
                items(recipes) { recipe ->
                    RecipeCard(recipe = recipe, onClick = {
                        navController.navigate("details/${recipe.id}") // Navigation vers le détail de la recette
                    })
                }

                // Affichage du loader à la fin de la liste si des recettes sont en train de se charger
                item {
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

// Fonction pour charger les recettes depuis le repository
suspend fun loadRecipes(
    repository: RecipeRepository,
    query: String,
    page: Int,
    onResult: (List<Recipe>) -> Unit,
    onError: () -> Unit
) {
    try {
        val newRecipes = repository.getRecipes(query, page)
        onResult(newRecipes) // Retourner les recettes chargées
    } catch (e: Exception) {
        println(e)
        onError() // Si erreur, appeler le callback d'erreur
    }
}