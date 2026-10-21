
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
import com.example.dsy1105_009v_2026.model.SolicitudErrores
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarSolicitudScreen(
    solicitudId: Long,
    viewModel: SolicitudViewModel,
    onVolver: () -> Unit,
    onActualizado: () -> Unit
) {
    val flujo = remember(solicitudId, viewModel) {
        viewModel.obtenerSolicitudPorId(solicitudId)
    }

    val solicitud by flujo.collectAsState(initial = null)

    var expandido by remember { mutableStateOf(false) }

    val categorias = listOf(
        "Desarrollo web",
        "Mantenimiento web",
        "Incidente tecnico",
        "Hosting",
        "Soporte de correo",
        "Marketing digital",
        "Otro requerimiento"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("← Volver")
        }

        Text(
            text = "Editar solicitud",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Modifica la información antes de guardar los cambios.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
        } else if (datos.estado != "Recibida") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "Esta solicitud ya no se puede editar porque su estado es '${datos.estado}'.",
                    modifier = Modifier.padding(18.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            OutlinedButton(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Volver")
            }
        } else {
            var titulo by remember(datos.id) {
                mutableStateOf(datos.titulo)
            }

            var descripcion by remember(datos.id) {
                mutableStateOf(datos.descripcion)
            }

            var categoria by remember(datos.id) {
                mutableStateOf(datos.categoria)
            }

            var errores by remember(datos.id) {
                mutableStateOf(SolicitudErrores())
            }

            var mensajeError by remember(datos.id) {
                mutableStateOf<String?>(null)
            }

            var guardando by remember(datos.id) {
                mutableStateOf(false)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información del requerimiento",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Solicitud SOL-${datos.id.toString().padStart(3, '0')}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = titulo,
                        onValueChange = {
                            titulo = it
                            errores = errores.copy(titulo = null)
                            mensajeError = null
                        },
                        label = { Text("Título de la solicitud") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        isError = errores.titulo != null,
                        supportingText = {
                            errores.titulo?.let { Text(it) }
                        }
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = {
                            descripcion = it
                            errores = errores.copy(descripcion = null)
                            mensajeError = null
                        },
                        label = { Text("Descripción del problema") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(14.dp),
                        isError = errores.descripcion != null,
                        supportingText = {
                            errores.descripcion?.let { Text(it) }
                        }
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandido,
                        onExpandedChange = {
                            expandido = !expandido
                        }
                    ) {
                        OutlinedTextField(
                            value = categoria,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandido
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(14.dp),
                            isError = errores.categoria != null
                        )

                        ExposedDropdownMenu(
                            expanded = expandido,
                            onDismissRequest = {
                                expandido = false
                            }
                        ) {
                            categorias.forEach { opcion ->
                                DropdownMenuItem(
                                    text = { Text(opcion) },
                                    onClick = {
                                        categoria = opcion
                                        errores = errores.copy(categoria = null)
                                        mensajeError = null
                                        expandido = false
                                    }
                                )
                            }
                        }
                    }

                    errores.categoria?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            mensajeError?.let { mensaje ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = mensaje,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            Button(
                onClick = {
                    errores = viewModel.validarEdicion(
                        titulo,
                        descripcion,
                        categoria
                    )

                    if (
                        errores.titulo == null &&
                        errores.descripcion == null &&
                        errores.categoria == null
                    ) {
                        guardando = true
                        mensajeError = null

                        viewModel.actualizarSolicitud(
                            solicitudId,
                            titulo,
                            descripcion,
                            categoria
                        ) { error ->
                            guardando = false

                            if (error == null) {
                                onActualizado()
                            } else {
                                mensajeError = error
                            }
                        }
                    }
                },
                enabled = !guardando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
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
                    Text(
                        text = "Guardar cambios",
                        fontWeight = FontWeight.Bold
                    )
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
        }
    }
}
