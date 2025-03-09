package com.example.student_recipe.ui.components

import com.example.student_recipe.R

import android.widget.ImageView
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.student_recipe.model.Recipe
@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically // ✅ Alignement vertical pour éviter l'étirement
        ) {
            ImageFromUrl(
                recipe.imageUrl,
                recipe.title,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f) // ✅ Donne la priorité au texte
                    .padding(end = 8.dp) // ✅ Ajoute un peu d'espace sur la droite
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black // ✅ Vérifie que la couleur est visible
                )
                Text(
                    text = "Réalisée par : "+recipe.publisher,
                    color = Color.Black,
                    style=MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}



@Composable
fun ImageFromUrl(url: String, title: String) {
    var hasError by remember { mutableStateOf(false) }

    if (!hasError) {
        AsyncImage(
            model = url,
            contentDescription = "Image chargée depuis Internet de $title",
            modifier = Modifier
                .size(100.dp) // ✅ Taille fixe pour éviter qu'elle prenne toute la place
                .clip(RoundedCornerShape(8.dp)),
            onError = { hasError = true } // Détecte une erreur et affiche l’image par défaut
        )
    }

    if (hasError) {
        Image(
            modifier = Modifier
                    .size(100.dp) // ✅ Taille fixe pour éviter qu'elle prenne toute la place
                .clip(RoundedCornerShape(8.dp)),
            painter = painterResource(id = R.drawable.logochargementrecipe),
            contentDescription = "Image par défaut"
        )
    }
}