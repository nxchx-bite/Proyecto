package com.example.dsy1105_009v_2026.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DrawerMenu(
    username: String,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Gestion de Solicitudes",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Bienvenido, $username",
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider()

        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = true,
            onClick = {},
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Inicio"
                )
            }
        )

        NavigationDrawerItem(
            label = { Text("Nueva solicitud") },
            selected = false,
            onClick = {
                navController.navigate("nuevaSolicitud")
            },
            icon = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Nueva solicitud"
                )
            }
        )

        NavigationDrawerItem(
            label = { Text("Mis solicitudes (proximamente)") },
            selected = false,
            onClick = {},
            icon = {
                Icon(
                    Icons.Default.List,
                    contentDescription = "Mis solicitudes"
                )
            }
        )
    }
}
