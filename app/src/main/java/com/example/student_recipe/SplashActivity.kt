package com.example.student_recipe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen() // Affiche l'écran de démarrage
        }
    }
}

@SuppressLint("ResourceAsColor")
@Composable
fun SplashScreen() {
    val context = LocalContext.current // Accède au contexte actuel

    val backgroundColor = Color(0xFFF5F5DC) // Définition de la couleur de fond

    // Mise en page de l'écran Splash
    Column(
        modifier = Modifier
            .fillMaxSize() // Remplir l'écran
            .background(backgroundColor) // Appliquer la couleur de fond
            .wrapContentSize(Alignment.Center), // Centrer le contenu
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Affiche le logo
        Image(
            painter = painterResource(id = R.drawable.logochargementrecipe),
            contentDescription = "Logo",
            modifier = Modifier.size(750.dp) // Taille du logo
        )

        Spacer(modifier = Modifier.height(20.dp)) // Espace entre le logo et la barre de chargement
        CircularProgressIndicator(color = Color.Gray) // Affiche une barre de chargement

        // Lancement de la transition après 2 secondes
        LaunchedEffect(Unit) {
            delay(2000) // Attendre 2 secondes
            val intent = Intent(context, MainActivity::class.java) // Crée une nouvelle intention pour l'écran principal
            context.startActivity(intent) // Démarre MainActivity
            (context as? ComponentActivity)?.finish() // Ferme l'activité Splash après le lancement
        }
    }
}