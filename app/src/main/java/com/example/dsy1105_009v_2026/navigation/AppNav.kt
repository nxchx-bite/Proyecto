
package com.example.dsy1105_009v_2026.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.dsy1105_009v_2026.ui.theme.HomeScreen
import com.example.dsy1105_009v_2026.view.DrawerMenu
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel

import com.example.dsy1105_009v_2026.ui.screen.NuevaSolicitudScreen
import com.example.dsy1105_009v_2026.ui.screen.ResumenSolicitudScreen
import com.example.dsy1105_009v_2026.ui.screen.SolicitudConfirmadaScreen
import com.example.dsy1105_009v_2026.ui.screen.ListaSolicitudesScreen
import com.example.dsy1105_009v_2026.ui.screen.DetalleSolicitudScreen
import com.example.dsy1105_009v_2026.ui.screen.EditarSolicitudScreen

@Composable
fun AppNav() {

    val navController = rememberNavController()

    // ViewModel compartido entre pantallas
    val solicitudViewModel: SolicitudViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // 1. LOGIN
        composable("login") {
            HomeScreen(navController = navController)
        }

        // 2. MENU PRINCIPAL
        composable(
            route = "DrawerMenu/{username}",
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val username = backStackEntry.arguments
                ?.getString("username")
                .orEmpty()

            DrawerMenu(
                username = username,
                navController = navController
            )
        }

        // 3. NUEVA SOLICITUD
        composable("nuevaSolicitud") {
            NuevaSolicitudScreen(
                viewModel = solicitudViewModel,
                onSolicitudValida = {
                    navController.navigate("resumenSolicitud")
                }
            )
        }

        // 4. RESUMEN DE SOLICITUD
        composable("resumenSolicitud") {
            ResumenSolicitudScreen(
                viewModel = solicitudViewModel,

                onVolver = {
                    navController.popBackStack()
                },

                onSolicitudGuardada = { id ->
                    navController.navigate(
                        "solicitudConfirmada/$id"
                    ) {
                        popUpTo("nuevaSolicitud") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // 5. SOLICITUD CONFIRMADA
        composable(
            route = "solicitudConfirmada/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val id = backStackEntry.arguments
                ?.getLong("id") ?: 0L

            SolicitudConfirmadaScreen(
                solicitudId = id,

                onVolverInicio = {
                    solicitudViewModel.limpiarFormulario()
                    navController.popBackStack()
                }
            )
        }

        // 6. LISTA DE SOLICITUDES
        composable("misSolicitudes") {
            ListaSolicitudesScreen(
                viewModel = solicitudViewModel,

                onVolver = {
                    navController.popBackStack()
                },

                onVerDetalle = { id ->
                    navController.navigate(
                        "detalleSolicitud/$id"
                    )
                }
            )
        }

        // 7. DETALLE DE SOLICITUD
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
                    navController.navigate(
                        "editarSolicitud/$id"
                    )
                }
            )
        }

        // 8. EDITAR SOLICITUD - NUEVO
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

