package com.example.student_recipe.api

import com.example.student_recipe.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("recipe/search")
    suspend fun searchRecipes(@Query("query") query: String, @Query("page") page: Int): RecipeResponse
}