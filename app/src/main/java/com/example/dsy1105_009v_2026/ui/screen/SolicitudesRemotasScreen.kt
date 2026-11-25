
package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.dsy1105_009v_2026.viewmodel.SolicitudesRemotasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudesRemotasScreen(
    onVolver: () -> Unit,
    viewModel: SolicitudesRemotasViewModel = viewModel()
) {

    val solicitudes by viewModel.solicitudes.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Solicitudes MongoDB")
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Datos obtenidos desde Spring Boot y MongoDB Atlas",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {
                    viewModel.cargarSolicitudes()
                },
                enabled = !cargando
            ) {
                Text("Actualizar solicitudes")
            }

            if (cargando) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (!cargando && error == null) {

                Text(
                    text = "Solicitudes recibidas: ${solicitudes.size}",
                    style = MaterialTheme.typography.titleMedium
                )

                if (solicitudes.isEmpty()) {

                    Text("No se encontraron solicitudes.")

                } else {

                    LazyColumn(
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        items(
                            items = solicitudes,
                            key = { it.id }
                        ) { solicitud ->

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Text(
                                        text = solicitud.titulo,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = solicitud.descripcion
                                    )

                                    HorizontalDivider()

                                    Text(
                                        text = "Categoría: ${solicitud.categoria}"
                                    )

                                    Text(
                                        text = "Estado: ${solicitud.estado}"
                                    )

                                    Text(
                                        text = "Prioridad: ${solicitud.prioridad ?: "Sin definir"}"
                                    )

                                    Text(
                                        text = "Responsable: ${solicitud.responsable ?: "Sin asignar"}"
                                    )

                                    Text(
                                        text = "Fecha: ${solicitud.fechaCreacion ?: "Sin fecha"}"
                                    )

                                    Text(
                                        text = "ID: ${solicitud.id}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

