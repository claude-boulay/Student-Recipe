package com.example.student_recipe.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.student_recipe.model.Recipe
import com.example.student_recipe.repository.RecipeRepository
import com.example.student_recipe.ui.theme.CustomBrown
import com.example.student_recipe.ui.theme.CustomDarkBrown
import androidx.compose.ui.text.TextStyle
import com.example.student_recipe.R
import com.example.student_recipe.function.decodeHtml
import com.example.student_recipe.ui.theme.CustomLightBrown

@Composable
fun RecipeDetailScreen(navController: NavController, recipeId: Int, repository: RecipeRepository) {
    var recipe by remember { mutableStateOf<Recipe?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Effet lancé pour charger la recette en fonction de l'ID
    LaunchedEffect(recipeId) {
        try {
            recipe = repository.getRecipeById(recipeId) // Chargement de la recette
        } catch (e: Exception) {
            errorMessage = "Loading error : ${e.message}" // Gestion des erreurs
        } finally {
            isLoading = false // Arrêt du chargement après la tentative
        }
    }

    // Affichage selon les états : chargement, erreur ou contenu
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Afficher un indicateur de chargement
            errorMessage != null -> Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error) // Message d'erreur
            recipe != null -> RecipeDetailContent(recipe!!) // Afficher le contenu de la recette
        }
    }
}

@Composable
fun RecipeDetailContent(recipe: Recipe) {
    var hasError by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Affichage du titre de la recette
            Text(
                text = decodeHtml(recipe.title),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = CustomDarkBrown,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Chargement de l'image de la recette avec Coil
            val painter = rememberAsyncImagePainter(recipe.imageUrl,onError = {hasError=true })
            if(!hasError){
                Image(
                    painter = painter,
                    contentDescription = "Recipe image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }else{
                Image(
                    painter = painterResource(id = R.drawable.imagenotfound),
                    contentDescription = "Recipe image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Affichage des ingrédients
            Text(
                text = "Ingredients :",
                style = MaterialTheme.typography.titleMedium,
                color = CustomBrown,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .background(CustomLightBrown, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
            ) {
                items(recipe.ingredients) { ingredient ->
                    val ingredientDecode= decodeHtml(ingredient)
                    Text(
                        text = "- $ingredientDecode",
                        style = TextStyle(
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Affichage de la description si elle existe
            if (recipe.description != "N/A") {
                Text(
                    text = "Description :",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = decodeHtml(recipe.description), modifier = Modifier.padding(4.dp))
            }
        }
    }
}