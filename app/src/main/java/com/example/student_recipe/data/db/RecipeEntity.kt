package com.example.student_recipe.data.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val featured_image: String, // Vérifie que ce champ correspond à `imageUrl` si nécessaire
    val publisher: String,
    val ingredients: List<String>, // Doit être converti avec un TypeConverter
    val imageUrl: String, // Assure-toi d'ajouter cette colonne !
    val description: String // Assure-toi d'ajouter cette colonne aussi !
)