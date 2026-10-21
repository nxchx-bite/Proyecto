
package com.example.dsy1105_009v_2026.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DrawerMenu(
    username: String,
    role: String,
    navController: NavController
) {
    val esAdmin = role == "ADMIN"
    val esCliente = role == "CLIENTE"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Gestión de solicitudes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "¡Bienvenido!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = if (esAdmin) {
                        "Administrador"
                    } else {
                        "Cliente"
                    },
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "MENÚ PRINCIPAL",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
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

        if (esCliente) {
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

        if (esAdmin) {
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

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider()

        Button(
            onClick = {
                navController.navigate("login") {
                    popUpTo("login") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                Icons.Default.ExitToApp,
                contentDescription = null
            )

            Spacer(modifier = Modifier.padding(start = 4.dp))

            Text("Cerrar sesión")
        }
    }
}
