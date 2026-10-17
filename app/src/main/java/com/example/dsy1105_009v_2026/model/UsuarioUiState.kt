package com.example.dsy1105_009v_2026.model

data class UsuarioUiState(
    val nombre: String = "", // Nombre del usuario
    val correo: String = "", // Correo electrónico
    val clave: String = "", // Contraseña
    val direccion: String = "", // Dirección del usuario
    val aceptaTerminos: Boolean = false, // Confirmación de términos
    val errores: UsuarioErrores = UsuarioErrores() // Objeto que conƟene los errores por campo
)