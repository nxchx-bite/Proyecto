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
fun DetalleSolicitudScreen(
    solicitudId: Long,
    viewModel: SolicitudViewModel,
    onVolver: () -> Unit,
    onEditar: () -> Unit,
    modoAdministrador: Boolean = false,
    onAdministrar: () -> Unit = {}
) {
    val flujoSolicitud = remember(solicitudId, viewModel) {
        viewModel.obtenerSolicitudPorId(solicitudId)
    }

    val solicitud by flujoSolicitud.collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Detalle de solicitud",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedButton(onClick = onVolver) {
            Text("Volver")
        }

        HorizontalDivider()

        if (solicitud == null) {
            Text("Cargando solicitud o registro no encontrado.")
        } else {
            val datos = solicitud!!

            Text(
                text = "SOL-${datos.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = datos.titulo,
                style = MaterialTheme.typography.headlineSmall
            )

            HorizontalDivider()

            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium
            )
            Text(datos.descripcion)

            HorizontalDivider()

            Text(
                text = "Categoría",
                style = MaterialTheme.typography.titleMedium
            )
            Text(datos.categoria)

            HorizontalDivider()

            Text(
                text = "Estado",
                style = MaterialTheme.typography.titleMedium
            )
            Text(datos.estado)

            // CAMPO AGREGADO POR TU COMPAÑERO
            HorizontalDivider()

            Text(
                text = "Prioridad",
                style = MaterialTheme.typography.titleMedium
            )
            Text(datos.prioridad)

            // CAMPO AGREGADO POR TU COMPAÑERO
            HorizontalDivider()

            Text(
                text = "Responsable asignado",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                datos.responsable ?: "Sin responsable asignado"
            )

            HorizontalDivider()

            Text(
                text = "Fecha de creación",
                style = MaterialTheme.typography.titleMedium
            )
            Text(datos.fechaCreacion)

            // EVIDENCIA FOTOGRAFICA
            HorizontalDivider()

            Text(
                text = "Evidencia fotográfica",
                style = MaterialTheme.typography.titleMedium
            )

            EvidenciaImagen(
                evidenciaUri = datos.evidenciaUri
            )

            // BOTONES SEGUN CONTEXTO DE NAVEGACION
            if (modoAdministrador) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onAdministrar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Administrar solicitud")
                }
            } else if (datos.estado == "Recibida") {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onEditar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar solicitud")
                }
            }
        }
    }
}
