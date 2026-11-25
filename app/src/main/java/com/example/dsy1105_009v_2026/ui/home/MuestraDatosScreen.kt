package com.example.dsy1105_009v_2026.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.example.dsy1105_009v_2026.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuestraDatosScreen(
    username: String,
    navController: NavController,
    postViewModel: PostViewModel = viewModel()
) {

    val posts by postViewModel.posts.collectAsState()
    val cargando by postViewModel.cargando.collectAsState()
    val error by postViewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Datos externos - API REST")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Bienvenido, $username",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Publicaciones de JSONPlaceholder"
            )

            Button(
                onClick = {
                    postViewModel.obtenerPosts()
                }
            ) {
                Text("Actualizar datos")
            }

            when {
                cargando -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Text(
                        text = error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }

                else -> {
                    Text(
                        text = "Publicaciones recibidas: ${posts.size}"
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        items(
                            items = posts,
                            key = { post -> post.id }
                        ) { post ->

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Text(
                                        text = "Publicación #${post.id}",
                                        style = MaterialTheme.typography.labelLarge
                                    )

                                    Text(
                                        text = post.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = post.body
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("login") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}
