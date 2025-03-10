package com.example.student_recipe.model

import com.example.student_recipe.data.db.RecipeEntity
import com.google.gson.annotations.SerializedName

// Modèle de données représentant une recette
data class Recipe(
    @SerializedName("pk")
    val id: Int, // Identifiant unique de la recette
    val title: String,
    @SerializedName("featured_image")
    val imageUrl: String, // URL de l'image de la recette
    val ingredients: List<String>,
    val description: String,
    val publisher: String
)

// Extension pour convertir un Recipe en RecipeEntity (format base de données)
fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.title,
        publisher = this.publisher,
        ingredients = this.ingredients,
        imageUrl = this.imageUrl,
        description = this.description
    )
}