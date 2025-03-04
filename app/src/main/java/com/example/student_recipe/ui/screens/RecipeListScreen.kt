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

@Composable
fun RecipeListScreen(navController: NavController, repository: RecipeRepository) {
    var recipes by remember { mutableStateOf(emptyList<Recipe>()) }
    var query by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(query.text) {
        isLoading = true
        errorMessage = null
        try {
            recipes = repository.getRecipes(query.text, 1) // Appel sécurisé
        } catch (e: Exception) {
            errorMessage = "Erreur de chargement des recettes."
        }
        isLoading = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(query = query, onQueryChanged = { query = it })

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage ?: "Une erreur est survenue", color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(recipes) { recipe ->
                    RecipeCard(recipe = recipe, onClick = {
                        navController.navigate("details/${recipe.id}")
                    })
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: TextFieldValue, onQueryChanged: (TextFieldValue) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text("Rechercher des recettes") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeList() {
    val navController = rememberNavController()


}