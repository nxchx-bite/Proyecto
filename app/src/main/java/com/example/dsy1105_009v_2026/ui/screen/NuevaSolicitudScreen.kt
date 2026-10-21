
package com.example.dsy1105_009v_2026.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@Composable
fun NuevaSolicitudScreen(
    viewModel: SolicitudViewModel,
    onSolicitudValida: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val context = LocalContext.current

    var menuExpandido by remember { mutableStateOf(false) }
    var fotoEvidencia by remember { mutableStateOf<Bitmap?>(null) }

    // Abrir la cámara y recibir la fotografía
    val abrirCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        fotoEvidencia = bitmap
    }

    // Solicitar permiso de cámara cuando sea necesario
    val solicitarPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permisoConcedido ->
        if (permisoConcedido) {
            abrirCamara.launch(null)
        }
    }

    // Categorías disponibles
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

        // Campo: título
        OutlinedTextField(
            value = estado.titulo,
            onValueChange = viewModel::onTituloChange,
            label = { Text("Título de la solicitud") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = estado.errores.titulo != null,
            supportingText = {
                estado.errores.titulo?.let { mensaje ->
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        // Campo: descripción
        OutlinedTextField(
            value = estado.descripcion,
            onValueChange = { nuevaDescripcion ->
                viewModel.onDescripcionChange(nuevaDescripcion)
            },
            label = { Text("Descripción del problema") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            isError = estado.errores.descripcion != null,
            supportingText = {
                estado.errores.descripcion?.let { mensaje ->
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        // Selector de categoría
        Text(
            text = "Categoría",
            style = MaterialTheme.typography.titleMedium
        )

        Box {
            OutlinedButton(
                onClick = { menuExpandido = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (estado.categoria.isBlank()) {
                        "Seleccionar categoría"
                    } else {
                        estado.categoria
                    }
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

        // Error de categoría
        estado.errores.categoria?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error
            )
        }

        HorizontalDivider()

        // Fotografía de evidencia
        Text(
            text = "Fotografía de evidencia",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Puedes fotografiar el problema para ayudar a explicar tu solicitud."
        )

        Button(
            onClick = {
                val permisoConcedido = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (permisoConcedido) {
                    abrirCamara.launch(null)
                } else {
                    solicitarPermisoCamara.launch(
                        Manifest.permission.CAMERA
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (fotoEvidencia == null) {
                    "Tomar fotografía"
                } else {
                    "Tomar otra fotografía"
                }
            )
        }

        // Mostrar la fotografía tomada
        fotoEvidencia?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Fotografía de evidencia",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 260.dp)
            )

            // Eliminar fotografía
            OutlinedButton(
                onClick = { fotoEvidencia = null },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar fotografía")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Validar el formulario y continuar
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
