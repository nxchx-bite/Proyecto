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

import com.example.dsy1105_009v_2026.ui.screen.ListaSolicitudesScreen
import com.example.dsy1105_009v_2026.ui.screen.DetalleSolicitudScreen
import com.example.dsy1105_009v_2026.ui.screen.EditarSolicitudScreen

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


// MIS SOLICITUDES - CLIENTE
        composable("misSolicitudes") {
            ListaSolicitudesScreen(
                viewModel = solicitudViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onVerDetalle = { id ->
                    navController.navigate("detalleSolicitud/$id")
                }
            )
        }

// DETALLE DE SOLICITUD
        composable(
            route = "detalleSolicitud/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getLong("id") ?: 0L

            DetalleSolicitudScreen(
                solicitudId = id,
                viewModel = solicitudViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onEditar = {
                    navController.navigate("editarSolicitud/$id")
                }
            )
        }

// EDITAR SOLICITUD
        composable(
            route = "editarSolicitud/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getLong("id") ?: 0L

            EditarSolicitudScreen(
                solicitudId = id,
                viewModel = solicitudViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onActualizado = {
                    navController.popBackStack()
                }
            )
        }



    }
}
