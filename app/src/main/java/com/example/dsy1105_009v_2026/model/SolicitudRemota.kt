package com.example.dsy1105_009v_2026.model

data class SolicitudRemota(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val estado: String = "",
    val fechaCreacion: String? = null,
    val evidenciaUri: String? = null,
    val prioridad: String? = null,
    val responsable: String? = null
)
