package com.example.student_recipe.data.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val featured_image: String,
    val publisher: String,
    val ingredients: List<String>,
    val imageUrl: String,
    val description: String
)