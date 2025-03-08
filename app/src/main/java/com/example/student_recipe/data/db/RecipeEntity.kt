package com.example.student_recipe.data.db


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.student_recipe.model.Recipe

@Entity(tableName = "recipes", indices = [Index(value = ["id"]),Index(value = ["title"], unique = true)])
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val publisher: String,
    val ingredients: List<String>,
    val imageUrl: String,
    val description: String
)
fun RecipeEntity.toDomainModel(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        ingredients = this.ingredients,
        description = this.description,
        publisher=this.publisher
    )
}