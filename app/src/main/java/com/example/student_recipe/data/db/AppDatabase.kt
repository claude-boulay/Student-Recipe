package com.example.student_recipe.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// Définition de la base de données Room
@Database(entities = [RecipeEntity::class], version = 4)
@TypeConverters(Converters::class) // Convertisseurs pour les types complexes
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao // Accès aux opérations DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton pour obtenir l'instance de la base de données
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recipe_database"
                )
                    .fallbackToDestructiveMigration() // Supprime et recrée la DB en cas de changement de version
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}