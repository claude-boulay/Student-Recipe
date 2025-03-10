package com.example.student_recipe.ui.components

import androidx.compose.ui.text.font.FontStyle
import com.example.student_recipe.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.student_recipe.model.Recipe

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    // Carte pour afficher les détails de la recette
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick), // Action au clic
        shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically // Alignement vertical
        ) {
            // Chargement de l'image de la recette
            ImageFromUrl(
                recipe.imageUrl,
                recipe.title,
            )

            Spacer(modifier = Modifier.width(16.dp)) // Espacement entre l'image et le texte

            Column(
                modifier = Modifier
                    .weight(1f) // Prendre l'espace restant pour le texte
                    .padding(end = 8.dp) // Ajout d'un padding à droite
            ) {
                // Affichage du titre de la recette
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Text(
                    text = "By " + recipe.publisher,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
    }
}

@Composable
fun ImageFromUrl(url: String, title: String) {
    var hasError by remember { mutableStateOf(false) }

    // Chargement de l'image depuis l'URL
    if (!hasError) {
        AsyncImage(
            model = url,
            contentDescription = "Image loaded from the internet of $title",
            modifier = Modifier
                .size(100.dp) // Taille fixe de l'image
                .clip(RoundedCornerShape(8.dp)), // Coins arrondis
            onError = { hasError = true } // Gestion des erreurs
        )
    }

    // Affichage d'une image par défaut si erreur de chargement
    if (hasError) {
        Image(
            modifier = Modifier
                .size(100.dp) // Taille fixe de l'image
                .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(id = R.drawable.imagenotfound),
            contentDescription = "Recipe image not found"
        )
    }
}