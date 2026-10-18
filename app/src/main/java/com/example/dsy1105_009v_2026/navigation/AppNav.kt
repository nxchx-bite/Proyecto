
package com.example.dsy1105_009v_2026.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.dsy1105_009v_2026.ui.theme.HomeScreen
import com.example.dsy1105_009v_2026.ui.screen.NuevaSolicitudScreen
import com.example.dsy1105_009v_2026.view.DrawerMenu
import com.example.dsy1105_009v_2026.viewmodel.SolicitudViewModel
import com.example.dsy1105_009v_2026.ui.screen.ResumenSolicitudScreen
import com.example.dsy1105_009v_2026.ui.screen.SolicitudConfirmadaScreen


import androidx.lifecycle.viewModelScope
import com.example.dsy1105_009v_2026.model.data.local.SolicitudEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

import com.example.dsy1105_009v_2026.ui.screen.ListaSolicitudesScreen



@Composable
fun AppNav() {

    val navController = rememberNavController()

    // ViewModel compartido entre pantallas
    val solicitudViewModel: SolicitudViewModel = viewModel()


    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // Pantalla de inicio de sesion
        composable("login") {
            HomeScreen(navController = navController)
        }

        // Menu principal
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

        // Formulario de nueva solicitud

        composable("nuevaSolicitud") {

            NuevaSolicitudScreen(
                viewModel = solicitudViewModel,
                onSolicitudValida = {
                    navController.navigate("resumenSolicitud")
                }
            )
        }


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


        composable("misSolicitudes") {

            ListaSolicitudesScreen(
                viewModel = solicitudViewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }


    }
}
