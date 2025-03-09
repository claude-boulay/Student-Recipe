package com.example.student_recipe.repository


import android.util.Log
import com.example.student_recipe.data.db.RecipeDao
import com.example.student_recipe.data.db.RecipeEntity
import com.example.student_recipe.data.db.toDomainModel
import com.example.student_recipe.data.remote.RecipeApi
import com.example.student_recipe.model.Recipe
import com.example.student_recipe.model.toEntity

open class RecipeRepository(
    private val recipeApi: RecipeApi,
    private val recipeDao: RecipeDao
) {
    suspend fun getRecipes(query: String, page: Int): List<Recipe> {
        Log.e("Pagination",page.toString())
        return try {
            //  Faire l'appel API
            val response = recipeApi.searchRecipes(query, page)
            Log.e("Recipe",response.toString())
            if (response.isSuccessful) {
                val newRecipes = response.body()?.results ?: emptyList()
                Log.e("Recipe",newRecipes.toString())
                // Convertir en RecipeEntity et insérer en base
                val recipeEntities = newRecipes.map { it.toEntity() }

                recipeDao.insertAll(recipeEntities)

                // Retourner les recettes stockées
                return newRecipes

            } else {
                throw Exception("Erreur API: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Error","Erreur lors de la récupération des recettes: ${e.message}")
            val localRecipes = recipeDao.getAllRecipes()
            if (localRecipes.isNotEmpty() && (page==1 || page==2)) {
                return localRecipes.map { it.toDomainModel() } // Convertir RecipeEntity en Recipe
            }

            throw Exception()
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            val response = recipeApi.getRecipeById(recipeId)
            if (response.isSuccessful) {
                response.body() ?: recipeDao.getRecipeById(recipeId) // ✅ Vérification et récupération des données
            } else {
                recipeDao.getRecipeById(recipeId) // ✅ Retourne les données locales en cas d’échec de l'API
            }
        } catch (e: Exception) {
            recipeDao.getRecipeById(recipeId) // ✅ Évite le crash en cas d’erreur API
        }
    }
    suspend fun searchRecipes(query: String): List<Recipe> {
        return recipeDao.searchRecipes(query).map { it.toDomainModel() }
    }
}

private fun RecipeEntity.toRecipe(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        ingredients = this.ingredients,
        imageUrl =this.imageUrl,
        description = this.description,
        publisher = this.publisher
    )
}