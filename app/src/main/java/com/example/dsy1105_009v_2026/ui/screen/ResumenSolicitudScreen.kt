package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@Composable
fun ResumenSolicitudScreen(
    viewModel: SolicitudViewModel,
    onVolver: () -> Unit,
    onSolicitudGuardada: (Long) -> Unit
) {

    // Datos del formulario
    val estado by viewModel.estado.collectAsState()

    // Estado del guardado
    val guardando by viewModel.guardando.collectAsState()
    val errorGuardado by viewModel.errorGuardado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Resumen de solicitud",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Revisa los datos antes de registrar tu solicitud."
        )

        HorizontalDivider()

        Text(
            text = "Título",
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = estado.titulo)

        HorizontalDivider()

        Text(
            text = "Descripción",
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = estado.descripcion)

        HorizontalDivider()

        Text(
            text = "Categoría",
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = estado.categoria)

        HorizontalDivider()

        // NUEVO: EVIDENCIA FOTOGRAFICA
        Text(
            text = "Evidencia fotográfica",
            style = MaterialTheme.typography.titleMedium
        )

        EvidenciaImagen(
            evidenciaUri = estado.evidenciaUri
        )

        HorizontalDivider()

        Text(
            text = "Estado inicial: Recibida",
            style = MaterialTheme.typography.bodyLarge
        )

        errorGuardado?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (guardando) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                viewModel.guardarSolicitud { id ->
                    onSolicitudGuardada(id)
                }
            },
            enabled = !guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (guardando) "Guardando..."
                else "Registrar solicitud"
            )
        }

        OutlinedButton(
            onClick = onVolver,
            enabled = !guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver y editar")
        }
    }
}

