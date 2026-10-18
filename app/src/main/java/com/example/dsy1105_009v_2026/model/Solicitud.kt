package com.example.dsy1105_009v_2026.model

// Representa una solicitud registrada por un cliente
data class Solicitud(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val estado: String = "Recibida",
    val fechaCreacion: String,
    val evidenciaUri: String? = null
)
