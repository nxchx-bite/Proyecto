package com.example.dsy1105_009v_2026.ui.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.example.dsy1105_009v_2026.model.MensajeWhatsAppSimulado
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@Composable
fun SolicitudConfirmadaScreen(
    solicitudId: Long,
    viewModel: SolicitudViewModel,
    onVolverInicio: () -> Unit
) {

    // Obtener la solicitud guardada desde Room
    val flujoSolicitud = remember(solicitudId, viewModel) {
        viewModel.obtenerSolicitudPorId(solicitudId)
    }

    val solicitud by flujoSolicitud.collectAsState(
        initial = null
    )

    val context = LocalContext.current

    // Mostrar automáticamente el aviso por primera vez
    var mostrarAviso by rememberSaveable(solicitudId) {
        mutableStateOf(true)
    }

    var enlaceCopiado by remember {
        mutableStateOf(false)
    }

    // Generar el código de seguimiento
    val codigo = remember(solicitudId) {
        MensajeWhatsAppSimulado.codigoSeguimiento(
            solicitudId
        )
    }

    // Generar enlace de demostración
    val enlace = remember(solicitudId) {
        MensajeWhatsAppSimulado.enlaceFicticio(
            solicitudId
        )
    }

    // Preparar mensaje utilizando los datos de Room
    val mensaje = solicitud?.let { datos ->
        MensajeWhatsAppSimulado.prepararMensaje(
            solicitudId = datos.id,
            titulo = datos.titulo,
            categoria = datos.categoria
        )
    }

    // VENTANA EMERGENTE DE WHATSAPP
    if (mostrarAviso && solicitud != null) {

        AlertDialog(
            onDismissRequest = {
                mostrarAviso = false
            },

            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },

            title = {
                Text(
                    text = "¡Solicitud registrada!",
                    textAlign = TextAlign.Center
                )
            },

            text = {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Se envió un enlace de seguimiento " +
                                "de tu caso a tu WhatsApp (simulación).",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Código de seguimiento: $codigo",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Referencia: $enlace",
                        style = MaterialTheme.typography.bodySmall
                    )



                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarAviso = false
                    }
                ) {
                    Text("Entendido")
                }
            }
        )
    }

    // PANTALLA DE CONFIRMACIÓN
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.spacedBy(16.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "¡Solicitud registrada!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Tu solicitud fue guardada correctamente.",
                    textAlign = TextAlign.Center
                )

                HorizontalDivider()

                Text(
                    text = "CÓDIGO DE SEGUIMIENTO",
                    style = MaterialTheme.typography.titleMedium
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = codigo,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Text(
                    text = "Estado: Recibida"
                )

                HorizontalDivider()

                Text(
                    text = "Referencia para WhatsApp (simulado)",
                    style = MaterialTheme.typography.titleMedium
                )

                if (mensaje == null) {

                    CircularProgressIndicator()

                    Text("Cargando solicitud...")

                } else {

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {

                        Text(
                            text = mensaje,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Permite abrir nuevamente la ventana
                    Button(
                        onClick = {
                            mostrarAviso = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver aviso de WhatsApp")
                    }

                    // Copiar el enlace ficticio
                    OutlinedButton(
                        onClick = {

                            val clipboard =
                                context.getSystemService(
                                    Context.CLIPBOARD_SERVICE
                                ) as ClipboardManager

                            clipboard.setPrimaryClip(
                                ClipData.newPlainText(
                                    "Enlace de seguimiento",
                                    enlace
                                )
                            )

                            enlaceCopiado = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Copiar enlace de seguimiento")
                    }

                    if (enlaceCopiado) {
                        Text(
                            text = "Enlace copiado correctamente",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Text(
                    text = "El enlace de seguimiento fue enviado exitosamente",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onVolverInicio,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}

