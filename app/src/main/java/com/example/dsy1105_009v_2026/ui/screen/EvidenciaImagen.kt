
package com.example.dsy1105_009v_2026.ui.screen

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun EvidenciaImagen(
    evidenciaUri: String?,
    modifier: Modifier = Modifier
) {
    if (evidenciaUri.isNullOrBlank()) {
        Text(
            text = "No se adjuntó evidencia fotográfica.",
            style = MaterialTheme.typography.bodyMedium
        )
        return
    }

    val bitmap = remember(evidenciaUri) {
        try {
            val ruta = Uri.parse(evidenciaUri).path

            val opciones = BitmapFactory.Options().apply {
                inSampleSize = 2
            }

            if (ruta != null) {
                BitmapFactory.decodeFile(ruta, opciones)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    if (bitmap != null) {
        Card(
            modifier = modifier.fillMaxWidth()
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Fotografía de evidencia",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp)
            )
        }
    } else {
        Text(
            text = "No se pudo cargar la evidencia.",
            color = MaterialTheme.colorScheme.error
        )
    }
}

