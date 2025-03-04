package com.example.student_recipe.data.remote

import com.example.student_recipe.model.Recipe
import com.example.student_recipe.data.remote.RecipeResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipe/search/")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Header("Authorization") auth: String = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
    ): RecipeResponse
    companion object {
        private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

        fun create(): RecipeApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecipeApi::class.java)
        }
    }
    @GET("recipe/get/")
    suspend fun getRecipeById(@Query("id") id: Int): Recipe?

}