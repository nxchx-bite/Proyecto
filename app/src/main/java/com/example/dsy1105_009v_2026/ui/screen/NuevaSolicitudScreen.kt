
package com.example.dsy1105_009v_2026.ui.screen

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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

    var menuExpandido by remember { mutableStateOf(false) }
    var errorEvidencia by remember { mutableStateOf<String?>(null) }

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

                viewModel.onEvidenciaChange(Uri.fromFile(archivo).toString())
                errorEvidencia = null
            } catch (e: Exception) {
                errorEvidencia = "Error al guardar la fotografía"
                Log.e("NuevaSolicitud", "Error de cámara", e)
            }
        }
    }

    val solicitarPermisoCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permisoConcedido ->
        if (permisoConcedido) {
            abrirCamara.launch(null)
        } else {
            errorEvidencia = "No se concedió permiso para la cámara"
        }
    }

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

                val entrada = context.contentResolver.openInputStream(uri)
                    ?: throw IllegalStateException("No se pudo abrir la imagen")

                entrada.use { origen ->
                    archivo.outputStream().use { destino ->
                        origen.copyTo(destino)
                    }
                }

                viewModel.onEvidenciaChange(Uri.fromFile(archivo).toString())
                errorEvidencia = null
            } catch (e: Exception) {
                errorEvidencia = "Error al seleccionar la imagen"
                Log.e("NuevaSolicitud", "Error de galería", e)
            }
        }
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
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Nueva solicitud",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Completa los datos para registrar tu requerimiento.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "1. Información del requerimiento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = estado.titulo,
                    onValueChange = viewModel::onTituloChange,
                    label = { Text("Título de la solicitud") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    isError = estado.errores.titulo != null,
                    supportingText = {
                        estado.errores.titulo?.let { mensaje ->
                            Text(mensaje, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                OutlinedTextField(
                    value = estado.descripcion,
                    onValueChange = viewModel::onDescripcionChange,
                    label = { Text("Descripción del problema") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(14.dp),
                    isError = estado.errores.descripcion != null,
                    supportingText = {
                        estado.errores.descripcion?.let { mensaje ->
                            Text(mensaje, color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { menuExpandido = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
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

                estado.errores.categoria?.let { mensaje ->
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "2. Evidencia fotográfica",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Agrega una fotografía para ayudar a explicar el problema.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = {
                        errorEvidencia = null

                        val permisoDeclarado = context.packageManager
                            .getPackageInfo(
                                context.packageName,
                                PackageManager.GET_PERMISSIONS
                            )
                            .requestedPermissions
                            ?.contains(Manifest.permission.CAMERA) == true

                        if (!permisoDeclarado) {
                            abrirCamara.launch(null)
                        } else {
                            val permisoConcedido =
                                ContextCompat.checkSelfPermission(
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
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        if (estado.evidenciaUri == null) {
                            "Tomar fotografía"
                        } else {
                            "Tomar otra fotografía"
                        }
                    )
                }

                OutlinedButton(
                    onClick = {
                        errorEvidencia = null
                        abrirGaleria.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Seleccionar desde galería")
                }

                estado.evidenciaUri?.let { ruta ->
                    val bitmap = remember(ruta) {
                        try {
                            BitmapFactory.decodeFile(Uri.parse(ruta).path)
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

                    OutlinedButton(
                        onClick = {
                            viewModel.onEvidenciaChange(null)
                            errorEvidencia = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Quitar evidencia")
                    }
                }

                errorEvidencia?.let { mensaje ->
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    onSolicitudValida()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Continuar al resumen",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
