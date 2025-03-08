package com.example.student_recipe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.student_recipe.model.Recipe
import com.example.student_recipe.repository.RecipeRepository

@Composable
fun RecipeDetailScreen(navController: NavController, recipeId: Int, repository: RecipeRepository) {
    var recipe by remember { mutableStateOf<Recipe?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(recipeId) {
        try {
            recipe = repository.getRecipeById(recipeId)
        } catch (e: Exception) {
            errorMessage = "Erreur lors du chargement de la recette."
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            recipe != null -> RecipeDetailContent(recipe!!)
        }
    }
}

@Composable
fun RecipeDetailContent(recipe: Recipe) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = recipe.title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Image avec Coil
        val painter = rememberImagePainter(recipe.imageUrl)

        Image(
            painter = painter,
            contentDescription = "Image de la recette",
            modifier = Modifier.fillMaxWidth().height(250.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Ingrédients :", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(recipe.ingredients) { ingredient ->
                Text(text = "- $ingredient", modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecipeDetail() {
    val navController = rememberNavController()
    val dummyRecipe = Recipe(
        id = 1,
        title = "Pizza Margarita",
        ingredients = listOf("Tomates", "Mozzarella", "Basilic", "Huile d'olive"),
        description = "Une pizza italienne classique.",
        publisher = "mary",
        imageUrl = "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/583/featured_image.png"
    )

}