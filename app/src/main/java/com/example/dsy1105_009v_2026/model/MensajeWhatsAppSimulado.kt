
package com.example.dsy1105_009v_2026.model

object MensajeWhatsAppSimulado {

    // Genera un código como SOL-005
    fun codigoSeguimiento(id: Long): String {
        require(id > 0) {
            "El ID de la solicitud debe ser válido"
        }

        return "SOL-${id.toString().padStart(3, '0')}"
    }

    // Enlace ficticio para la demostración
    fun enlaceFicticio(id: Long): String {
        return "https://seguimiento.example/solicitudes/" +
                codigoSeguimiento(id)
    }

    // Construye el mensaje de seguimiento
    fun prepararMensaje(
        solicitudId: Long,
        titulo: String,
        categoria: String
    ): String {

        val codigo = codigoSeguimiento(solicitudId)
        val enlace = enlaceFicticio(solicitudId)

        val tituloSeguro = titulo.trim()
            .replace(Regex("[\\r\\n]+"), " ")

        val categoriaSegura = categoria.trim()
            .replace(Regex("[\\r\\n]+"), " ")

        return """
            ¡Hola! Tu solicitud fue registrada correctamente.

            Código: $codigo
            Título: $tituloSeguro
            Categoría: $categoriaSegura
            Estado: Recibida

            Enlace de seguimiento(simulado):
            $enlace

            Puedes revisar tu solicitud desde la aplicación.
        """.trimIndent()
    }
}

