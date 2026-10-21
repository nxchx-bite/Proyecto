
package com.example.dsy1105_009v_2026.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DrawerMenu(
    username: String,
    role: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Gestión de Solicitudes",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Bienvenido, $username",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = if (role == "ADMIN")
                "Rol: Administrador"
            else
                "Rol: Cliente"
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

        if (role == "CLIENTE") {
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
                label = { Text("Mis solicitudes") },
                selected = false,
                onClick = {
                    navController.navigate("misSolicitudes")
                },
                icon = {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "Mis solicitudes"
                    )
                }
            )

        }

        if (role == "ADMIN") {
            NavigationDrawerItem(
                label = { Text("Gestionar solicitudes") },
                selected = false,
                onClick = {
                    navController.navigate("gestionarSolicitudes")
                },
                icon = {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "Gestionar solicitudes"
                    )
                }
            )
        }

        // Cerrar sesión: disponible para ambos roles
        NavigationDrawerItem(
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = {
                navController.navigate("login") {
                    popUpTo("login") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.Default.ExitToApp,
                    contentDescription = "Cerrar sesión"
                )
            }
        )
    }
}
