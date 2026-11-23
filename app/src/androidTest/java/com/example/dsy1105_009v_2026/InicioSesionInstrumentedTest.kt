package com.example.dsy1105_009v_2026

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InicioSesionInstrumentedTest {

    // Inicia MainActivity en el emulador
    @get:Rule
    val composeRule =
        createAndroidComposeRule<MainActivity>()

    // PRUEBA 1
    // Verificar que la pantalla de inicio
    // se muestre correctamente.
    @Test
    fun pantallaInicio_seMuestraCorrectamente() {

        // Comprobar el mensaje de bienvenida
        composeRule
            .onNodeWithText("Bienvenido")
            .assertIsDisplayed()

        // Comprobar el nombre de la aplicación
        composeRule
            .onNodeWithText(
                "Gestión y seguimiento de solicitudes"
            )
            .assertIsDisplayed()

        // Comprobar que exista el campo Usuario
        composeRule
            .onNodeWithText("Usuario")
            .assertExists()
    }

    // PRUEBA 2
    // Verificar que el botón Ver contraseña
    // funcione correctamente.
    @Test
    fun mostrarContrasena_funcionaCorrectamente() {

        // Presionar el botón "Ver"
        composeRule
            .onNodeWithText("Ver")
            .performClick()

        // Verificar que ahora diga "Ocultar"
        composeRule
            .onNodeWithText("Ocultar")
            .assertIsDisplayed()

        // Presionar "Ocultar"
        composeRule
            .onNodeWithText("Ocultar")
            .performClick()

        // Comprobar que vuelve a decir "Ver"
        composeRule
            .onNodeWithText("Ver")
            .assertIsDisplayed()
    }
}
