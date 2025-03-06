package com.example.student_recipe.repository


import com.example.student_recipe.data.db.RecipeDao
import com.example.student_recipe.data.db.RecipeEntity
import com.example.student_recipe.data.remote.RecipeApi
import com.example.student_recipe.model.Recipe

open class RecipeRepository(
    private val recipeApi: RecipeApi,
    private val recipeDao: RecipeDao
) {
    suspend fun getRecipes(query: String, page: Int): List<Recipe> {
        return try {
            val response = recipeApi.searchRecipes(query, page)
            response.results
        } catch (e: java.net.UnknownHostException) {
            throw Exception("Pas de connexion Internet. Vérifie ta connexion réseau.")
        } catch (e: Exception) {
            throw Exception("Erreur inconnue. Impossible de charger les recettes.")
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            val response = recipeApi.getRecipeById(recipeId)
            response ?: recipeDao.getRecipeById(recipeId)
        } catch (e: Exception) {
            recipeDao.getRecipeById(recipeId)
        }
    }
}

private fun RecipeEntity.toRecipe(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        ingredients = this.ingredients,
        imageUrl =this.featured_image,
        description = this.publisher,
    )
}