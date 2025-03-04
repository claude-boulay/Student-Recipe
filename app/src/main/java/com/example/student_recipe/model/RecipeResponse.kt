package com.example.student_recipe.model

data class RecipeResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Recipe>
)