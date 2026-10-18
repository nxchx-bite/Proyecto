package com.example.dsy1105_009v_2026.model

// Almacena el estado del formulario de solicitudes
data class SolicitudUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val evidenciaUri: String? = null,
    val errores: SolicitudErrores = SolicitudErrores()
)
