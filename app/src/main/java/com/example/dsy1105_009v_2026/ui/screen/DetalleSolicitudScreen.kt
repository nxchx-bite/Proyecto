
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
    // Consultar los datos de la solicitud
    val flujoSolicitud = remember(solicitudId, viewModel) {
        viewModel.obtenerSolicitudPorId(solicitudId)
    }

    val solicitud by flujoSolicitud.collectAsState(initial = null)

    // NUEVO: Consultar historial desde Room
    val flujoHistorial = remember(solicitudId, viewModel) {
        viewModel.obtenerHistorial(solicitudId)
    }

    val historial by flujoHistorial.collectAsState(
        initial = emptyList()
    )

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

        OutlinedButton(
            onClick = onVolver
        ) {
            Text("Volver")
        }

        HorizontalDivider()

        val datos = solicitud

        if (datos == null) {

            Text("Cargando solicitud o registro no encontrado.")

        } else {

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

            HorizontalDivider()

            Text(
                text = "Prioridad",
                style = MaterialTheme.typography.titleMedium
            )
            Text(datos.prioridad)

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

            HorizontalDivider()

            // EVIDENCIA
            Text(
                text = "Evidencia fotográfica",
                style = MaterialTheme.typography.titleMedium
            )

            EvidenciaImagen(
                evidenciaUri = datos.evidenciaUri
            )

            HorizontalDivider()

            // NUEVO: HISTORIAL
            Text(
                text = "Historial de la solicitud",
                style = MaterialTheme.typography.titleMedium
            )

            if (historial.isEmpty()) {

                Text(
                    text = "Todavía no hay cambios de estado registrados."
                )

            } else {

                historial.forEach { evento ->

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(6.dp)
                        ) {

                            Text(
                                text = "${evento.estadoAnterior} → ${evento.estadoNuevo}",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Text(
                                text = "Fecha: ${evento.fechaHora}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = "Usuario: ${evento.usuario}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = "Perfil: ${evento.perfil}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            if (evento.comentario.isNotBlank()) {
                                Text(
                                    text = evento.comentario,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // BOTONES SEGUN EL CONTEXTO
            Spacer(modifier = Modifier.height(8.dp))

            if (modoAdministrador) {

                Button(
                    onClick = onAdministrar,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Administrar solicitud")
                }

            } else if (datos.estado == "Recibida") {

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
