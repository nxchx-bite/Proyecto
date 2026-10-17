package com.example.dsy1105_009v_2026.ui.screen

import com.example.dsy1105_009v_2026.view.UsuarioViewModel


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {

    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Registro de Usuario"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // NOMBRE
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = {
                Text("Nombre")
            },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.nombre != null
        )

        if (estado.errores.nombre != null) {
            Text(
                text = estado.errores.nombre!!
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // CORREO
        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = {
                Text("Correo")
            },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.correo != null
        )

        if (estado.errores.correo != null) {
            Text(
                text = estado.errores.correo!!
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // CLAVE
        OutlinedTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            label = {
                Text("Contraseña")
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = estado.errores.clave != null
        )

        if (estado.errores.clave != null) {
            Text(
                text = estado.errores.clave!!
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // DIRECCIÓN
        OutlinedTextField(
            value = estado.direccion,
            onValueChange = viewModel::onDireccionChange,
            label = {
                Text("Dirección")
            },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errores.direccion != null
        )

        if (estado.errores.direccion != null) {
            Text(
                text = estado.errores.direccion!!
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // BOTÓN
        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    navController.navigate("resumen")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}