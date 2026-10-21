package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun ListaSolicitudesScreen(
    viewModel: SolicitudViewModel,
    onVolver: () -> Unit,
    onVerDetalle: (Long) -> Unit
) {
    // Obtener la lista desde el ViewModel
    val solicitudes by viewModel.solicitudes.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Mis solicitudes",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedButton(
            onClick = onVolver
        ) {
            Text("Volver al menu")
        }

        HorizontalDivider()

        if (solicitudes.isEmpty()) {

            Text(
                text = "No hay solicitudes registradas.",
                style = MaterialTheme.typography.bodyLarge
            )

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                items(
                    items = solicitudes,
                    key = { it.id }
                ) { solicitud ->


                    ElevatedCard(
                        onClick = {
                            onVerDetalle(solicitud.id)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {


                    Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            Text(
                                text = "SOL-${
                                    solicitud.id.toString()
                                        .padStart(3, '0')
                                }",
                                style = MaterialTheme
                                    .typography.titleMedium
                            )

                            Text(
                                text = solicitud.titulo,
                                style = MaterialTheme
                                    .typography.titleLarge
                            )

                            Text(
                                text = "Categoria: ${solicitud.categoria}"
                            )

                            Text(
                                text = "Estado: ${solicitud.estado}",
                                color = MaterialTheme
                                    .colorScheme.primary
                            )

                            Text(
                                text = "Fecha: ${solicitud.fechaCreacion}",
                                style = MaterialTheme
                                    .typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
