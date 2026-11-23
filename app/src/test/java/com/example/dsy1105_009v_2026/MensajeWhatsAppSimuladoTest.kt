package com.example.dsy1105_009v_2026

import com.example.dsy1105_009v_2026.model.MensajeWhatsAppSimulado
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MensajeWhatsAppSimuladoTest {

    // PRUEBA 1
    // Verificar que el codigo de seguimiento
    // se genere correctamente.
    @Test
    fun codigoSeguimiento_esCorrecto() {

        // ARRANGE: preparar el ID
        val id = 5L

        // ACT: ejecutar la funcion real
        val resultado =
            MensajeWhatsAppSimulado.codigoSeguimiento(id)

        // ASSERT: comprobar el resultado
        assertEquals("SOL-005", resultado)
    }

    // PRUEBA 2
    // Verificar que el mensaje incluya
    // los datos y el enlace de seguimiento.
    @Test
    fun mensajeWhatsApp_contieneDatosCorrectos() {

        // ARRANGE: preparar datos de prueba
        val id = 10L
        val titulo = "Problema con servidor"
        val categoria = "Soporte"

        // ACT: construir el mensaje
        val mensaje =
            MensajeWhatsAppSimulado.prepararMensaje(
                solicitudId = id,
                titulo = titulo,
                categoria = categoria
            )

        // ASSERT: comprobar su contenido
        assertTrue(mensaje.contains("SOL-010"))

        assertTrue(
            mensaje.contains("Problema con servidor")
        )

        assertTrue(mensaje.contains("Soporte"))

        assertTrue(
            mensaje.contains(
                "https://seguimiento.example/solicitudes/SOL-010"
            )
        )
    }
}
