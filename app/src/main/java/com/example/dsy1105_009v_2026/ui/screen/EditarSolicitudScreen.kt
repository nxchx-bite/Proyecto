package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dsy1105_009v_2026.model.SolicitudErrores
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Editar solicitud",
            style = MaterialTheme.typography.headlineMedium
        )

        val datos = solicitud

        if (datos == null) {
            Text("Cargando solicitud o registro no encontrado.")

        } else if (datos.estado != "Recibida") {
            Text("Esta solicitud ya no se puede editar.")

            OutlinedButton(onClick = onVolver) {
                Text("Volver")
            }

        } else {
            // Mantener los cambios mientras se edita
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

            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    errores = errores.copy(titulo = null)
                },
                label = { Text("Titulo") },
                isError = errores.titulo != null,
                supportingText = {
                    errores.titulo?.let { Text(it) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    errores = errores.copy(descripcion = null)
                },
                label = { Text("Descripcion") },
                minLines = 3,
                isError = errores.descripcion != null,
                supportingText = {
                    errores.descripcion?.let { Text(it) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            var expandido by remember {
                mutableStateOf(false)
            }

            val categorias = listOf(
                "Desarrollo web",
                "Mantenimiento web",
                "Incidente tecnico",
                "Hosting",
                "Soporte de correo",
                "Marketing digital",
                "Otro requerimiento"
            )

            Box {
                OutlinedButton(
                    onClick = { expandido = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(categoria)
                }

                DropdownMenu(
                    expanded = expandido,
                    onDismissRequest = { expandido = false }
                ) {
                    categorias.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                categoria = opcion
                                errores = errores.copy(categoria = null)
                                expandido = false
                            }
                        )
                    }
                }
            }

            errores.categoria?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            mensajeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }


            Button(
                enabled = !guardando,
                modifier = Modifier.fillMaxWidth(),
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
                }
            ) {
                Text(
                    if (guardando) "Guardando..."
                    else "Guardar cambios"
                )
            }


            OutlinedButton(
                onClick = onVolver,
                enabled = !guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
