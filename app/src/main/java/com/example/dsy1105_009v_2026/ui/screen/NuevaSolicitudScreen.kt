
package com.example.dsy1105_009v_2026.ui.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun NuevaSolicitudScreen(
    viewModel: SolicitudViewModel,
    onSolicitudValida: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val context = LocalContext.current

    var menuExpandido by remember {
        mutableStateOf(false)
    }

    var errorEvidencia by remember {
        mutableStateOf<String?>(null)
    }

    // CAMARA
    val abrirCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->

        if (bitmap != null) {
            try {
                val archivo = File(
                    context.filesDir,
                    "evidencia_${System.currentTimeMillis()}.jpg"
                )

                FileOutputStream(archivo).use { salida ->
                    val guardada = bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        90,
                        salida
                    )

                    if (!guardada) {
                        throw IllegalStateException(
                            "No se pudo guardar la fotografía"
                        )
                    }
                }

                viewModel.onEvidenciaChange(
                    Uri.fromFile(archivo).toString()
                )

                errorEvidencia = null

            } catch (e: Exception) {
                errorEvidencia = "Error al guardar la fotografía"

                Log.e(
                    "NuevaSolicitud",
                    "Error de cámara",
                    e
                )
            }
        }
    }

    // GALERIA
    val abrirGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->

        if (uri != null) {
            try {
                val tipo = context.contentResolver.getType(uri)

                val extension = when (tipo) {
                    "image/png" -> "png"
                    "image/webp" -> "webp"
                    else -> "jpg"
                }

                val archivo = File(
                    context.filesDir,
                    "evidencia_${System.currentTimeMillis()}.$extension"
                )

                val entrada = context.contentResolver
                    .openInputStream(uri)
                    ?: throw IllegalStateException(
                        "No se pudo abrir la imagen"
                    )

                entrada.use { origen ->
                    archivo.outputStream().use { destino ->
                        origen.copyTo(destino)
                    }
                }

                viewModel.onEvidenciaChange(
                    Uri.fromFile(archivo).toString()
                )

                errorEvidencia = null

            } catch (e: Exception) {
                errorEvidencia = "Error al seleccionar la imagen"

                Log.e(
                    "NuevaSolicitud",
                    "Error de galería",
                    e
                )
            }
        }
    }

    // CATEGORIAS
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
            label = { Text("Título de la solicitud") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = estado.errores.titulo != null,
            supportingText = {
                estado.errores.titulo?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        // DESCRIPCION
        OutlinedTextField(
            value = estado.descripcion,
            onValueChange = viewModel::onDescripcionChange,
            label = { Text("Descripción del problema") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            isError = estado.errores.descripcion != null,
            supportingText = {
                estado.errores.descripcion?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        // CATEGORIA
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
                    if (estado.categoria.isBlank()) {
                        "Seleccionar categoría"
                    } else {
                        estado.categoria
                    }
                )
            }

            DropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = {
                    menuExpandido = false
                }
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

        HorizontalDivider()

        // EVIDENCIA FOTOGRAFICA
        Text(
            text = "Fotografía de evidencia",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Puedes tomar una fotografía o seleccionar " +
                    "una imagen para mostrar el problema."
        )

        // BOTON CAMARA
        Button(
            onClick = {
                abrirCamara.launch(null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (estado.evidenciaUri == null) {
                    "Tomar fotografía"
                } else {
                    "Tomar otra fotografía"
                }
            )
        }

        // BOTON GALERIA
        OutlinedButton(
            onClick = {
                abrirGaleria.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts
                            .PickVisualMedia.ImageOnly
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Seleccionar desde galería")
        }

        // VISTA PREVIA
        estado.evidenciaUri?.let { ruta ->

            val bitmap = remember(ruta) {
                try {
                    BitmapFactory.decodeFile(
                        Uri.parse(ruta).path
                    )
                } catch (e: Exception) {
                    null
                }
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Fotografía de evidencia",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 260.dp)
                )
            } else {
                Text(
                    text = "No se pudo mostrar la imagen",
                    color = MaterialTheme.colorScheme.error
                )
            }

            // QUITAR EVIDENCIA
            OutlinedButton(
                onClick = {
                    viewModel.onEvidenciaChange(null)
                    errorEvidencia = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Quitar evidencia")
            }
        }

        // MOSTRAR ERROR DE IMAGEN
        errorEvidencia?.let { mensaje ->
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // CONTINUAR AL RESUMEN
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

