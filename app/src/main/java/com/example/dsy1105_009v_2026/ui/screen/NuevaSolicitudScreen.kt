package com.example.dsy1105_009v_2026.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@Composable
fun NuevaSolicitudScreen(
    viewModel: SolicitudViewModel,
    onSolicitudValida: () -> Unit
) {
    // Observar los datos del ViewModel
    val estado by viewModel.estado.collectAsState()

    // Controlar la apertura del menu de categorias
    var menuExpandido by remember {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Nueva solicitud",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Completa los datos para registrar tu requerimiento.",
            style = MaterialTheme.typography.bodyMedium
        )

        // TITULO
        OutlinedTextField(
            value = estado.titulo,
            onValueChange = viewModel::onTituloChange,
            label = { Text("Titulo de la solicitud") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = estado.errores.titulo != null,
            supportingText = {
                estado.errores.titulo?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        // DESCRIPCION
        OutlinedTextField(
            value = estado.descripcion,
            onValueChange = viewModel::onDescripcionChange,
            label = { Text("Descripcion del problema") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            isError = estado.errores.descripcion != null,
            supportingText = {
                estado.errores.descripcion?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        // CATEGORIA
        Text(
            text = "Categoria",
            style = MaterialTheme.typography.titleMedium
        )

        Box {
            OutlinedButton(
                onClick = { menuExpandido = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (estado.categoria.isBlank())
                        "Seleccionar categoria"
                    else
                        estado.categoria
                )
            }

            DropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria) },
                        onClick = {
                            viewModel.onCategoriaChange(categoria)
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        estado.errores.categoria?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // VALIDAR FORMULARIO
        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    onSolicitudValida()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar")
        }
    }
}
