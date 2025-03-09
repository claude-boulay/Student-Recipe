package com.example.student_recipe.model

import com.example.student_recipe.data.db.RecipeEntity
import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("pk")
    val id: Int,
    val title: String,
    @SerializedName("featured_image")
    val imageUrl: String,
    val ingredients: List<String>,
    val description: String,
    val publisher:String
)
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