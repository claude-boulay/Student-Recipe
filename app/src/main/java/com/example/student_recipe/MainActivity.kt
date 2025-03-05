package com.example.student_recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

        val database = AppDatabase.getDatabase(this)
        val recipeApi = RecipeApi.create()
        val repository = RecipeRepository(recipeApi, database.recipeDao())

        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xffd6b98d))) {
                    NavHost(navController = navController, startDestination = "list") {
                        composable("list") {
                            RecipeListScreen(navController, repository)
                        }
                        composable("details/{recipeId}") { backStackEntry ->
                            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
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