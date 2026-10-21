
package com.example.dsy1105_009v_2026

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.dsy1105_009v_2026.ui.screen.NuevaSolicitudScreen
import com.example.dsy1105_009v_2026.ui.screen.ResumenSolicitudScreen
import com.example.dsy1105_009v_2026.ui.screen.SolicitudConfirmadaScreen
import com.example.dsy1105_009v_2026.ui.screen.GestionSolicitudesScreen
import com.example.dsy1105_009v_2026.view.DrawerMenu
import com.example.dsy1105_009v_2026.ui.theme.HomeScreen
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

@Composable
fun AppNav() {

    val navController = rememberNavController()
    val solicitudViewModel: SolicitudViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // Inicio de sesión
        composable("login") {
            HomeScreen(
                navController = navController
            )
        }

        // Menú principal
        composable(
            route = "DrawerMenu/{username}/{role}"
        ) { backStackEntry ->

            val username =
                backStackEntry.arguments?.getString("username") ?: "cliente"

            val role =
                backStackEntry.arguments?.getString("role") ?: "CLIENTE"

            DrawerMenu(
                username = username,
                role = role,
                navController = navController
            )
        }

        // Crear solicitud
        composable("nuevaSolicitud") {
            NuevaSolicitudScreen(
                viewModel = solicitudViewModel,
                onSolicitudValida = {
                    navController.navigate("resumenSolicitud")
                }
            )
        }

        // Resumen y registro de la solicitud
        composable("resumenSolicitud") {
            ResumenSolicitudScreen(
                viewModel = solicitudViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onSolicitudGuardada = { id ->

                    navController.navigate("solicitudConfirmada/$id") {
                        popUpTo("nuevaSolicitud") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Confirmación de la solicitud
        composable(
            route = "solicitudConfirmada/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getLong("id") ?: 0L

            SolicitudConfirmadaScreen(
                solicitudId = id,
                onVolverInicio = {
                    navController.navigate("login") {
                        popUpTo("login") {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Gestionar solicitudes
        composable("gestionarSolicitudes") {
            GestionSolicitudesScreen(
                viewModel = solicitudViewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
