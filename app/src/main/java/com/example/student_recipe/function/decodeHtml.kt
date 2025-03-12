package com.example.student_recipe.function

import android.text.Html
import android.os.Build

/**
 * Décode une chaîne HTML en texte brut.
 * - Pour Android Nougat (API 24) et versions ultérieures, utilise FROM_HTML_MODE_LEGACY pour une conversion correcte.
 * - Pour les versions plus anciennes, utilise la méthode obsolète sans mode spécifique.
 *
 * @param htmlText La chaîne contenant du texte formaté en HTML.
 * @return Le texte brut sans balises HTML.
 */
fun decodeHtml(htmlText: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(htmlText).toString()
    }
}