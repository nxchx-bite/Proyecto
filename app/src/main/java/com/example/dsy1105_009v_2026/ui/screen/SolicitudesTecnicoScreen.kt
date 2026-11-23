package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dsy1105_009v_2026.model.EstadoSolicitud
import com.example.dsy1105_009v_2026.model.data.local.HistorialSolicitudEntity
import com.example.dsy1105_009v_2026.model.data.local.SolicitudEntity
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@Composable
fun SolicitudesTecnicoScreen(
    viewModel: SolicitudViewModel,
    username: String,
    onVolver: () -> Unit
) {
    val solicitudes by remember(username) {
        viewModel.obtenerSolicitudesPorTecnico(username)
    }.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        Text(
            text = "Mis solicitudes asignadas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Bienvenido, $username. Revisa las solicitudes y registra el trabajo realizado.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (solicitudes.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "No tienes solicitudes asignadas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Cuando el administrador te asigne una solicitud, aparecerá en esta pantalla."
                    )
                }
            }
        } else {
            Text(
                text = "Total asignadas: ${solicitudes.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(solicitudes, key = { it.id }) { solicitud ->
                    SolicitudTecnicoItem(
                        solicitud = solicitud,
                        username = username,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun SolicitudTecnicoItem(
    solicitud: SolicitudEntity,
    username: String,
    viewModel: SolicitudViewModel
) {
    var menuEstadosAbierto by remember(solicitud.id) { mutableStateOf(false) }
    var comentario by remember(solicitud.id) { mutableStateOf("") }
    var mensaje by remember(solicitud.id) { mutableStateOf<String?>(null) }
    var guardando by remember(solicitud.id) { mutableStateOf(false) }
    var estadoSeleccionado by remember(solicitud.id, solicitud.estado) {
        mutableStateOf(solicitud.estado)
    }

    val estadosDisponibles = EstadoSolicitud
        .desdeDescripcion(solicitud.estado)
        ?.siguientesEstados()
        ?.filter {
            it != EstadoSolicitud.CANCELADA &&
                    it != EstadoSolicitud.CERRADA
        }
        .orEmpty()

    val historial by remember(solicitud.id) {
        viewModel.obtenerHistorial(solicitud.id)
    }.collectAsState(initial = emptyList())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "SOL-${solicitud.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = solicitud.titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Descripción del cliente",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = solicitud.descripcion,
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider()
            DatoTecnico("Categoría", solicitud.categoria)
            DatoTecnico("Estado actual", solicitud.estado)
            DatoTecnico("Prioridad", solicitud.prioridad)
            DatoTecnico("Fecha de creación", solicitud.fechaCreacion)
            DatoTecnico("Técnico responsable", solicitud.responsable ?: "Sin asignar")

            Text(
                text = "Evidencia fotográfica",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            EvidenciaImagen(evidenciaUri = solicitud.evidenciaUri)

            HorizontalDivider()
            Text(
                text = "Actualizar trabajo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (estadosDisponibles.isEmpty()) {
                Text(
                    text = "No hay más cambios de estado permitidos para esta solicitud.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = { menuEstadosAbierto = true }) {
                        Text("Nuevo estado: $estadoSeleccionado  ▾")
                    }
                    DropdownMenu(
                        expanded = menuEstadosAbierto,
                        onDismissRequest = { menuEstadosAbierto = false }
                    ) {
                        estadosDisponibles.forEach { estado ->
                            DropdownMenuItem(
                                text = { Text(estado.descripcion) },
                                onClick = {
                                    estadoSeleccionado = estado.descripcion
                                    menuEstadosAbierto = false
                                    mensaje = null
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = comentario,
                    onValueChange = {
                        comentario = it
                        mensaje = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Comentario del trabajo realizado") },
                    placeholder = {
                        Text("Describe las tareas realizadas o la solución aplicada")
                    },
                    minLines = 3,
                    maxLines = 5
                )

                Button(
                    onClick = {
                        guardando = true
                        mensaje = null
                        viewModel.cambiarEstadoTecnico(
                            id = solicitud.id,
                            nuevoEstado = estadoSeleccionado,
                            comentario = comentario,
                            username = username
                        ) { error ->
                            guardando = false
                            if (error == null) {
                                comentario = ""
                                mensaje = "Cambio y comentario guardados correctamente."
                            } else {
                                mensaje = error
                            }
                        }
                    },
                    enabled = !guardando &&
                            comentario.isNotBlank() &&
                            estadoSeleccionado != solicitud.estado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (guardando) "Guardando..." else "Guardar cambios")
                }

                mensaje?.let {
                    Text(
                        text = it,
                        color = if (it.startsWith("Cambio y comentario")) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            HorizontalDivider()
            Text(
                text = "Historial de actividad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (historial.isEmpty()) {
                Text(
                    text = "Todavía no hay comentarios ni cambios registrados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                historial.forEach { evento ->
                    EventoHistorialTecnico(evento)
                }
            }
        }
    }
}

@Composable
private fun DatoTecnico(
    etiqueta: String,
    valor: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun EventoHistorialTecnico(evento: HistorialSolicitudEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "${evento.estadoAnterior} → ${evento.estadoNuevo}",
                fontWeight = FontWeight.SemiBold
            )
            Text("${evento.perfil}: ${evento.usuario}")
            Text(evento.comentario)
            Text(
                text = evento.fechaHora,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
