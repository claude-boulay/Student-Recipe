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
import com.example.student_recipe.ui.components.IngredientFilterBar
import com.example.student_recipe.ui.theme.CustomDarkBrown
import kotlinx.coroutines.delay
import  com.example.student_recipe.ui.components.SearchBar


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

    //  Recherche : Si la requête est vide, on charge toutes les recettes
    LaunchedEffect(query) {
        delay(500) // Petit délai pour éviter les requêtes inutiles
        if (query.isBlank()) {
            currentPage = 1
            endReached = false
            loadRecipes(repository, query, currentPage, onResult = {
                recipes = it
                if (recipes.isEmpty()) {
                    endReached = true //  Stopper la pagination si aucun résultat
                }
            }, onError = {
                errorMessage = "Error loading recipes"
            })
        } else {
            val results = repository.searchRecipes(query)
            recipes = results
            endReached = true //  Stopper la pagination pendant la recherche
        }
    }

    //  Chargement de la page suivante uniquement si `endReached` est faux
    suspend fun loadNextPage() {
        if (isLoading || endReached || query.isNotBlank()) return
        isLoading = true
        val nextPage = currentPage + 1 //  Corrigé pour être plus simple et éviter les erreurs

        loadRecipes(repository, query, nextPage, onResult = { newRecipes ->
            if (newRecipes.isEmpty()) {
                endReached = true //  Stopper la pagination si la page est vide
            } else {
                recipes += newRecipes
                currentPage = nextPage //  Mise à jour propre du numéro de page
            }
        }, onError = {
            errorMessage = "Error loading recipes."
        })
        isLoading = false
    }

    //  Détection du scroll pour charger la page suivante (mais pas si `endReached == true`)
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                val totalItems = recipes.size
                if (!endReached && lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItems - 5 && !isLoading) {
                    loadNextPage() // Chargement de la prochaine page
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barre de recherche + Filtres
        SearchBar(query = query, onQueryChanged = { query = it })
        IngredientFilterBar(onQueryChanged = { query = it })

        // Affichage en fonction de l'état du chargement
        when {
            isLoading && recipes.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = errorMessage ?: "An error occurred", color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    //  Liste des recettes
                    items(recipes) { recipe ->
                        RecipeCard(recipe = recipe, onClick = {
                            navController.navigate("details/${recipe.id}")
                        })
                    }

                    //  Indicateur de chargement si on est encore en train de charger
                    item {
                        if (isLoading && !endReached) {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
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