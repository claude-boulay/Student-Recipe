package com.example.student_recipe.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.student_recipe.model.Recipe

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeEntity>) // Insère une liste de recettes en remplaçant les doublons

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity> // Récupère toutes les recettes

    @Query("SELECT * FROM recipes WHERE id = :recipeId LIMIT 1")
    suspend fun getRecipeById(recipeId: Int): Recipe? // Récupère une recette par son ID

    @Query("""
        SELECT * FROM recipes
        WHERE (:query IS NULL OR :query = '') 
        OR title LIKE '%' || :query || '%'
        OR ingredients LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'
    """)
    suspend fun searchRecipes(query: String): List<RecipeEntity> // Recherche des recettes par titre, ingrédients ou description
}