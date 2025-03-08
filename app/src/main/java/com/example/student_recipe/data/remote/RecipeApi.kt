package com.example.student_recipe.data.remote

import com.example.student_recipe.model.Recipe
import com.example.student_recipe.data.remote.RecipeResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeApi {

    @GET("recipe/search/")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<RecipeResponse> // ✅ Utilisation de Response<> pour gérer les erreurs

    @GET("recipe/get/")
    suspend fun getRecipeById(@Query("id") id: Int): Response<Recipe> // ✅ Ajout de Response<> pour éviter le crash

    companion object {
        private const val BASE_URL = "https://food2fork.ca/api/"
        private const val API_TOKEN = "9c8b06d329136da358c2d00e76946b0111ce2c48"

        fun create(): RecipeApi {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Token $API_TOKEN") // ✅ Ajout automatique du Token
                        .build()
                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client) // ✅ Utilisation du client avec Interceptor
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecipeApi::class.java)
        }
    }
}