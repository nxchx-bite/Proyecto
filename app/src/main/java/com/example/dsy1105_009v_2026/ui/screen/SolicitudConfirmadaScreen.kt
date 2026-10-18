
package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SolicitudConfirmadaScreen(
    solicitudId: Long,
    onVolverInicio: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Solicitud registrada",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tu solicitud fue guardada correctamente."
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Codigo de seguimiento",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "SOL-${solicitudId.toString().padStart(3, '0')}",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Estado: Recibida"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onVolverInicio,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}

