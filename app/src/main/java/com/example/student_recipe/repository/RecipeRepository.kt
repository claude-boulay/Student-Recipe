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
        Log.e("Pagination", page.toString())
        return try {
            // Appel API pour récupérer les recettes
            val response = recipeApi.searchRecipes(query, page)
            //Log.e("Recipe", response.toString())

            if (response.isSuccessful) {
                val newRecipes = response.body()?.results ?: emptyList()
                //Log.e("Recipe", newRecipes.toString())

                // Convertir les résultats en RecipeEntity et les stocker en base de données
                val recipeEntities = newRecipes.map { it.toEntity() }
                recipeDao.insertAll(recipeEntities)

                return newRecipes // Retourne les recettes obtenues depuis l'API
            } else {
                throw Exception("API error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("Error", "Error while fetching recipes: ${e.message}")

            // Récupération des recettes locales si l'API échoue
            val localRecipes = recipeDao.getAllRecipes()
            if (localRecipes.isNotEmpty() && (page == 1 )) {
                return localRecipes.map { it.toDomainModel() } // Conversion en Recipe
            }else{
                return emptyList()
            }

            throw Exception() // Propage l'erreur si aucune donnée locale n'est disponible
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            val response = recipeApi.getRecipeById(recipeId)
            if (response.isSuccessful) {
                response.body() ?: recipeDao.getRecipeById(recipeId) // Vérifie si l'API renvoie des données
            } else {
                recipeDao.getRecipeById(recipeId) // Retourne la recette locale en cas d'échec API
            }
        } catch (e: Exception) {
            recipeDao.getRecipeById(recipeId) // Évite le crash en cas d'erreur API
        }
    }

    suspend fun searchRecipes(query: String): List<Recipe> {
        return recipeDao.searchRecipes(query).map { it.toDomainModel() } // Recherche locale des recettes
    }
}

// Fonction d'extension pour convertir une RecipeEntity en Recipe
private fun RecipeEntity.toRecipe(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        ingredients = this.ingredients,
        imageUrl = this.imageUrl,
        description = this.description,
        publisher = this.publisher
    )
}