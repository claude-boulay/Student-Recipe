package com.example.student_recipe.data.remote

import com.example.student_recipe.model.Recipe

data class RecipeResponse(
    val count: Int,
    val results: List<Recipe>
)