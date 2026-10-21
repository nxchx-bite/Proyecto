package com.example.dsy1105_009v_2026.ui.screen



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dsy1105_009v_2026.model.data.local.SolicitudEntity
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionSolicitudesScreen(
    viewModel: SolicitudViewModel,
    onVolver: () -> Unit
) {
    val solicitudes by viewModel.solicitudes.collectAsState()

    var solicitudSeleccionada by remember {
        mutableStateOf<SolicitudEntity?>(null)
    }

    var prioridad by remember { mutableStateOf("Media") }
    var estado by remember { mutableStateOf("Recibida") }
    var responsable by remember { mutableStateOf("") }

    var mensaje by remember { mutableStateOf<String?>(null) }
    var mostrarMensaje by remember { mutableStateOf(false) }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Gestión de solicitudes",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Solicitudes registradas: ${solicitudes.size}")

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (solicitudes.isEmpty()) {
            Text("Todavía no hay solicitudes registradas.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = solicitudes,
                    key = { it.id }
                ) { solicitud ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                solicitudSeleccionada = solicitud
                                prioridad = solicitud.prioridad
                                estado = solicitud.estado
                                responsable = solicitud.responsable.orEmpty()
                                mensaje = null
                            },
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (solicitudSeleccionada?.id == solicitud.id)
                                    MaterialTheme.colorScheme.secondaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "SOL-${solicitud.id.toString().padStart(3, '0')}",
                                style = MaterialTheme.typography.labelLarge
                            )

                            Text(
                                text = solicitud.titulo,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Categoría: ${solicitud.categoria}")
                            Text("Estado: ${solicitud.estado}")
                            Text("Prioridad: ${solicitud.prioridad}")
                            Text(
                                "Responsable: ${
                                    solicitud.responsable ?: "Sin asignar"
                                }"
                            )

                            Text(
                                text = "Toca para gestionar",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }

        solicitudSeleccionada?.let { seleccionada ->
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Text(
                text = "Administrar SOL-${seleccionada.id.toString().padStart(3, '0')}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Prioridad")

            ExposedDropdownMenuBox(
                expanded = menuPrioridad,
                onExpandedChange = {
                    menuPrioridad = !menuPrioridad
                }
            ) {
                OutlinedTextField(
                    value = prioridad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar prioridad") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = menuPrioridad
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
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

            Spacer(modifier = Modifier.height(8.dp))

            Text("Estado")

            ExposedDropdownMenuBox(
                expanded = menuEstado,
                onExpandedChange = {
                    menuEstado = !menuEstado
                }
            ) {
                OutlinedTextField(
                    value = estado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Seleccionar estado") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = menuEstado
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = responsable,
                onValueChange = { responsable = it },
                label = { Text("Nombre del técnico responsable") },
                placeholder = { Text("Ej.: Técnico 1") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    mensaje = null

                    viewModel.cambiarPrioridad(
                        id = seleccionada.id,
                        nuevaPrioridad = prioridad
                    ) { errorPrioridad ->
                        if (errorPrioridad != null) {
                            mensaje = errorPrioridad
                        } else {
                            viewModel.cambiarEstado(
                                id = seleccionada.id,
                                nuevoEstado = estado
                            ) { errorEstado ->
                                if (errorEstado != null) {
                                    mensaje = errorEstado
                                } else {
                                    viewModel.asignarTecnico(
                                        id = seleccionada.id,
                                        responsable = responsable
                                    ) { errorResponsable ->
                                        mensaje = errorResponsable
                                            ?: "Cambios guardados correctamente."
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }

            TextButton(
                onClick = {
                    solicitudSeleccionada = null
                    mensaje = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }

            mensaje?.let {
                Text(
                    text = it,
                    color = if (
                        it == "Cambios guardados correctamente."
                    ) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
