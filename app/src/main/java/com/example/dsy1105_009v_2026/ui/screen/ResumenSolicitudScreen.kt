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
    onVolver: () -> Unit
) {

    // Observar los datos del ViewModel
    val estado by viewModel.estado.collectAsState()

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
            text = "Revisa los datos antes de registrar tu solicitud.",
            style = MaterialTheme.typography.bodyMedium
        )

        HorizontalDivider()

        Text(
            text = "Titulo",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = estado.titulo)

        HorizontalDivider()

        Text(
            text = "Descripcion",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = estado.descripcion)

        HorizontalDivider()

        Text(
            text = "Categoria",
            style = MaterialTheme.typography.titleMedium
        )

        Text(text = estado.categoria)

        HorizontalDivider()

        Text(
            text = "Estado inicial: Recibida",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver y editar")
        }
    }
}


