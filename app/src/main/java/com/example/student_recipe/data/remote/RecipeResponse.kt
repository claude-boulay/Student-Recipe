package com.example.student_recipe.data.remote

import com.example.student_recipe.model.Recipe

// Modèle de réponse pour l'API des recettes
data class RecipeResponse(
    val count: Int,          // Nombre total de résultats
    val results: List<Recipe> // Liste des recettes retournées
)