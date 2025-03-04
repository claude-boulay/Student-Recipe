package com.example.student_recipe.model

data class Recipe(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val description: String
)
