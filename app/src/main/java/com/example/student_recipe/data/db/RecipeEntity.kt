package com.example.student_recipe.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.student_recipe.model.Recipe

// Définition de l'entité Room pour la table "recipes"
@Entity(
    tableName = "recipes",
    indices = [Index(value = ["id"]), Index(value = ["title"], unique = true)] // Indexation sur id et titre (unique)
)
data class RecipeEntity(
    @PrimaryKey val id: Int, // Clé primaire
    val title: String,
    val publisher: String,
    val ingredients: List<String>,
    val imageUrl: String,
    val description: String
)

// Extension pour convertir une RecipeEntity en Recipe (modèle de domaine)
fun RecipeEntity.toDomainModel(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        ingredients = this.ingredients,
        description = this.description,
        publisher = this.publisher
    )
}