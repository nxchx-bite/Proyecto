
package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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

    val flujoHistorial = remember(solicitudId, viewModel) {
        viewModel.obtenerHistorial(solicitudId)
    }
    val historial by flujoHistorial.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("← Volver a las solicitudes")
        }

        Text(
            text = "Detalle de solicitud",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        val datos = solicitud

        if (datos == null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(
                    text = "Cargando solicitud o registro no encontrado.",
                    modifier = Modifier.padding(20.dp)
                )
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "SOL-${datos.id.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = datos.titulo,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Text(
                            text = "Estado: ${datos.estado}",
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            ),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Text(
                text = "Información general",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CampoDetalle("Descripción", datos.descripcion)
                    HorizontalDivider()
                    CampoDetalle("Categoría", datos.categoria)
                    HorizontalDivider()
                    CampoDetalle("Prioridad", datos.prioridad)
                    HorizontalDivider()
                    CampoDetalle(
                        "Responsable asignado",
                        datos.responsable ?: "Sin responsable asignado"
                    )
                    HorizontalDivider()
                    CampoDetalle("Fecha de creación", datos.fechaCreacion)
                }
            }

            Text(
                text = "Evidencia fotográfica",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EvidenciaImagen(evidenciaUri = datos.evidenciaUri)
                }
            }

            Text(
                text = "Historial de la solicitud",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (historial.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "Todavía no hay cambios de estado registrados.",
                        modifier = Modifier.padding(18.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                historial.forEach { evento ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${evento.estadoAnterior} → ${evento.estadoNuevo}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
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
                                HorizontalDivider()
                                Text(
                                    text = evento.comentario,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (modoAdministrador) {
                Button(
                    onClick = onAdministrar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Administrar solicitud")
                }
            } else if (datos.estado == "Recibida") {
                Button(
                    onClick = onEditar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Editar solicitud")
                }
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Volver")
            }
        }
    }
}

@Composable
private fun CampoDetalle(
    titulo: String,
    valor: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
