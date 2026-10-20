package com.example.dsy1105_009v_2026.model

data class Credential(
    val username: String,
    val password: String,
    val role: String
) {
    companion object {
        val Admin = Credential(
            username = "admin",
            password = "123",
            role = "ADMIN"
        )

        val Cliente = Credential(
            username = "cliente",
            password = "123",
            role = "CLIENTE"
        )
    }
}