
package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionSolicitudesScreen(
    viewModel: SolicitudViewModel,
    onVolver: () -> Unit,
    solicitudId: Long? = null,
    onVerDetalle: (Long) -> Unit = {}
) {
    val solicitudes by viewModel.solicitudes.collectAsState()

    var prioridad by remember { mutableStateOf("Media") }
    var estado by remember { mutableStateOf("Recibida") }
    var responsable by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }
    var guardando by remember { mutableStateOf(false) }
    var menuPrioridad by remember { mutableStateOf(false) }
    var menuEstado by remember { mutableStateOf(false) }

    val estados = listOf(
        "Recibida",
        "En revisión",
        "Pendiente de información",
        "Asignada",
        "En proceso",
        "Resuelta",
        "Cerrada",
        "Cancelada"
    )

    val prioridades = listOf("Baja", "Media", "Alta")

    val solicitudSeleccionada =
        solicitudes.firstOrNull { it.id == solicitudId }

    LaunchedEffect(solicitudId, solicitudSeleccionada?.id) {
        solicitudSeleccionada?.let { seleccionada ->
            prioridad = seleccionada.prioridad
            estado = seleccionada.estado
            responsable = seleccionada.responsable.orEmpty()
            mensaje = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        Text(
            text = if (solicitudId == null) {
                "Gestión de solicitudes"
            } else {
                "Administrar solicitud"
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = if (solicitudId == null) {
                "Revisa y administra los requerimientos registrados."
            } else {
                "Actualiza la prioridad, el estado y el técnico responsable."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (solicitudId == null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Total de solicitudes",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = solicitudes.size.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (solicitudes.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "No hay solicitudes todavía",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cuando se registren nuevas solicitudes, aparecerán aquí.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(
                        items = solicitudes,
                        key = { it.id }
                    ) { solicitud ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(
                                    animationSpec = tween(250)
                                )
                                .clickable {
                                    onVerDetalle(solicitud.id)
                                },
                            shape = RoundedCornerShape(18.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "SOL-${solicitud.id.toString().padStart(3, '0')}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = solicitud.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "Categoría: ${solicitud.categoria}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                HorizontalDivider()

                                AnimatedContent(
                                    targetState = solicitud.estado,
                                    transitionSpec = {
                                        fadeIn(tween(250)) togetherWith fadeOut(tween(150))
                                    },
                                    label = "CambioEstado"
                                ) { estadoActual ->
                                    Text(
                                        text = "Estado: $estadoActual",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Text(
                                    text = "Prioridad: ${solicitud.prioridad}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = "Responsable: ${solicitud.responsable ?: "Sin asignar"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                    text = "Ver detalle y administrar →",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        } else if (solicitudSeleccionada == null) {
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "SOL-${solicitudSeleccionada.id.toString().padStart(3, '0')}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = solicitudSeleccionada.titulo,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = solicitudSeleccionada.descripcion,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Text(
                    text = "Actualizar información",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                ExposedDropdownMenuBox(
                    expanded = menuPrioridad,
                    onExpandedChange = { menuPrioridad = !menuPrioridad }
                ) {
                    OutlinedTextField(
                        value = prioridad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Prioridad") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = menuPrioridad
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = menuPrioridad,
                        onDismissRequest = { menuPrioridad = false }
                    ) {
                        prioridades.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    prioridad = opcion
                                    menuPrioridad = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = menuEstado,
                    onExpandedChange = { menuEstado = !menuEstado }
                ) {
                    OutlinedTextField(
                        value = estado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado de la solicitud") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = menuEstado
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = menuEstado,
                        onDismissRequest = { menuEstado = false }
                    ) {
                        estados.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    estado = opcion
                                    menuEstado = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = responsable,
                    onValueChange = { responsable = it },
                    label = { Text("Técnico responsable") },
                    placeholder = { Text("Ej.: Técnico 1") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                mensaje?.let { texto ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (
                                texto == "Cambios guardados correctamente."
                            ) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            }
                        )
                    ) {
                        Text(
                            text = texto,
                            modifier = Modifier.padding(14.dp),
                            color = if (
                                texto == "Cambios guardados correctamente."
                            ) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        mensaje = null
                        guardando = true

                        viewModel.cambiarPrioridad(
                            id = solicitudSeleccionada.id,
                            nuevaPrioridad = prioridad
                        ) { errorPrioridad ->
                            if (errorPrioridad != null) {
                                mensaje = errorPrioridad
                                guardando = false
                            } else {
                                viewModel.cambiarEstado(
                                    id = solicitudSeleccionada.id,
                                    nuevoEstado = estado
                                ) { errorEstado ->
                                    if (errorEstado != null) {
                                        mensaje = errorEstado
                                        guardando = false
                                    } else {
                                        viewModel.asignarTecnico(
                                            id = solicitudSeleccionada.id,
                                            responsable = responsable
                                        ) { errorResponsable ->
                                            mensaje = errorResponsable
                                                ?: "Cambios guardados correctamente."
                                            guardando = false
                                        }
                                    }
                                }
                            }
                        }
                    },
                    enabled = !guardando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Guardando cambios...")
                    } else {
                        Text("Guardar cambios")
                    }
                }

                OutlinedButton(
                    onClick = onVolver,
                    enabled = !guardando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Cancelar")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
