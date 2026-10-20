
package com.example.dsy1105_009v_2026.repository

import com.example.dsy1105_009v_2026.model.Credential

class AuthRepository {

    fun login(username: String, password: String): Credential? {
        val credentials = listOf(
            Credential.Admin,
            Credential.Cliente
        )

        return credentials.find {
            it.username == username &&
                    it.password == password
        }
    }
}