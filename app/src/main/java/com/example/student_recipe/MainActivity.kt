package com.example.student_recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.student_recipe.repository.RecipeRepository
import com.example.student_recipe.data.db.AppDatabase
import com.example.student_recipe.data.remote.RecipeApi
import com.example.student_recipe.ui.screens.RecipeListScreen
import com.example.student_recipe.ui.screens.RecipeDetailScreen
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisation de la base de données et de l'API
        val database = AppDatabase.getDatabase(this)
        val recipeApi = RecipeApi.create()
        val repository = RecipeRepository(recipeApi, database.recipeDao())

        setContent {
            val navController = rememberNavController() // Contrôleur de navigation

            MaterialTheme {
                // Box pour encapsuler la mise en page avec une couleur de fond
                Box(modifier = Modifier.fillMaxSize().background(Color(0xffd6b98d))) {
                    // Définition du NavHost pour gérer les écrans
                    NavHost(navController = navController, startDestination = "list") {
                        // Ecran de liste des recettes
                        composable("list") {
                            RecipeListScreen(navController, repository) // Passe le repository au composant
                        }
                        // Ecran de détail d'une recette
                        composable("details/{recipeId}") { backStackEntry ->
                            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
                            // Si un ID de recette est passé, afficher l'écran de détail
                            if (recipeId != null) {
                                RecipeDetailScreen(navController, recipeId, repository)
                            }
                        }
                    }
                }
            }
        }
    }
}