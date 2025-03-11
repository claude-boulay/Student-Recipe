package com.example.student_recipe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp


@Composable
fun IngredientFilterBar(
    onQueryChanged: (String) -> Unit // ✅ Met à jour la barre de recherche
) {
    var selectedIngredient by remember { mutableStateOf<String?>(null) }
    val ingredients = listOf(
        "Tomato", "Chicken", "Cheese", "Fish", "Beef", "Chocolate", "Pork", "Soup", "Pasta", "Ice",
        "Rice", "Garlic", "Onion", "Mushroom", "Carrot", "Potato", "Lettuce", "Cucumber", "Pepper", "Spinach",
        "Egg", "Milk", "Butter", "Yogurt", "Cream", "Honey", "Lemon", "Orange", "Apple", "Banana",
        "Strawberry", "Blueberry", "Pineapple", "Coconut", "Almond", "Walnut", "Hazelnut", "Peanut", "Cashew", "Olive",
        "Salmon", "Shrimp", "Crab", "Lobster", "Octopus", "Squid", "Tofu", "Lentils", "Chickpeas", "Black Beans",
        "Soy Sauce", "Vinegar", "Cinnamon", "Nutmeg", "Cumin", "Paprika", "Turmeric", "Basil", "Oregano", "Thyme",
        "Rosemary", "Parsley", "Ginger", "Chili", "Vanilla", "Cocoa", "Flour", "Sugar", "Salt", "Pepper",
        "Oil", "Mustard", "Mayonnaise", "Ketchup", "Soy Milk", "Almond Milk", "Coconut Milk", "Maple Syrup", "Jam", "Bread",
        "Croissant", "Cereal", "Corn", "Pumpkin", "Zucchini", "Eggplant", "Radish", "Beetroot", "Celery", "Asparagus"
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ingredients) { ingredient ->
            val isSelected = selectedIngredient == ingredient
            Button(

                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) Color(0xFFA56A32) else Color(0xFFFFFBF7), // Marron clair et foncé
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(horizontal = 4.dp).border(2.dp, Color(0xFFFFA500),shape=RoundedCornerShape(20.dp)),
                onClick = {
                    onQueryChanged(ingredient) // Insère l’ingrédient dans la barre de recherche
                }
            ) {
                Text(text = ingredient, color = Color(0xFFFFA500))
            }
        }
    }
}