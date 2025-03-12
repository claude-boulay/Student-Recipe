package com.example.student_recipe.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.student_recipe.ui.theme.CustomDarkBrown

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp) // Ajoute un padding externe autour du composant
    ) {
        Text(
            text = "Student Recipes Book",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp, // Définit la taille du texte
            modifier = Modifier.padding(bottom = 8.dp) // Espacement sous le titre
        )

        TextField(
            value = query,
            onValueChange = { newText -> onQueryChanged(newText) }, // Met à jour la recherche à chaque saisie
            label = {
                Text(
                    text = "Search recipes",
                    color = CustomDarkBrown // Couleur du label
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)) // Bords arrondis du champ
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), RoundedCornerShape(12.dp)), // Bordure légère
            singleLine = true, // Empêche les retours à la ligne
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }, // Icône de recherche
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface, // Fond en mode focus
                unfocusedContainerColor = MaterialTheme.colorScheme.surface, // Fond en mode normal
                focusedIndicatorColor = Color.Transparent, // Pas de ligne de focus
                unfocusedIndicatorColor = Color.Transparent, // Pas de ligne en mode normal
                cursorColor = CustomDarkBrown, // Couleur du curseur
                focusedTextColor = CustomDarkBrown, // Texte en mode focus
                unfocusedTextColor = CustomDarkBrown, // Texte en mode normal
                focusedPlaceholderColor = CustomDarkBrown.copy(alpha = 0.6f), // Placeholder semi-transparent
                unfocusedPlaceholderColor = CustomDarkBrown.copy(alpha = 0.6f) // Placeholder semi-transparent
            )
        )
    }
}