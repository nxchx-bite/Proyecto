package com.example.dsy1105_009v_2026.ui.theme

import android.R.attr.enabled
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dsy1105_009v_2026.R
import com.example.dsy1105_009v_2026.ui.theme.login.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen(
    navController: NavController,
    vm: LoginViewModel = viewModel()

){

    val state = vm.uiState
    var showPass by remember { mutableStateOf(false) }

    val ColorScheme= darkColorScheme(
        primary = Color(0xFF98222E),
        onPrimary = Color.White,
        onSurface = Color(0xFF333333), //gris
    )



    MaterialTheme(
        colorScheme = ColorScheme
    ){ // Inicio aplicacion Material




 //Scaffold: permite generar una pantalla basica
    Scaffold (
        topBar={
            TopAppBar(title={ Text("Gestión móvil y seguimiento de solicitudes de clientes",
                color= MaterialTheme.colorScheme.onPrimary
            )})
        }// fin topBar


    ){ innerPadding ->
       // innerPadding  es un espacio interno que entrega Scaffold
        Column(
            modifier= Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xFFF0F0F0)),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally


        ){  //inicio inner
            Text(text="!!!  Bienvenido Usuario!!!!!!",
                style= MaterialTheme.typography.headlineMedium,
                color=MaterialTheme.colorScheme.primary
                )

            Spacer(modifier= Modifier.height(16.dp)
            )





            Image(
                painter= painterResource(id= R.drawable.logo),
                contentDescription = "Logo Appp",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit

            )


            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(horizontal=16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
            ){// Aplicar fila

                Text("Ingrese su usuario",
                    style= MaterialTheme.typography.bodyLarge.copy(
                        color= MaterialTheme.colorScheme.onSurface.copy(alpha=0.8f),
                        fontWeight = FontWeight.Bold),
                    modifier= Modifier
                        .padding(end=8.dp)

                    )//fin Text1





            }// fin Aplicar fila


            OutlinedTextField(
                value=state.username,
                onValueChange = vm::onUsernameChange,
                label={Text("Usuario")},
                singleLine = true,
                modifier= Modifier.fillMaxWidth(0.95f)
            ) // fin user


            Text("Ingrese su Contraseña",
                style= MaterialTheme.typography.bodyLarge.copy(
                    color= MaterialTheme.colorScheme.onSurface.copy(alpha=0.8f),
                    fontWeight = FontWeight.Bold),
                modifier= Modifier
                    .padding(end=8.dp)
                    .fillMaxWidth(0.95f)

            )//fin Text1



            OutlinedTextField(
                value=state.password,
                onValueChange = vm::onPasswordChange,
                label={Text("Contraseña")},
                singleLine = true,
                visualTransformation = if (showPass)
                    VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { showPass =!showPass}){
                    Text(if (showPass) "Ocultar" else "Ver"  )
                    }//fin aplica
                },// fin trail
                modifier = Modifier.fillMaxWidth(0.95f)

            )//fin pass


            if(state.error !=null){
                    Spacer(Modifier.height(8.dp))
                    Text(
                    text=state.error ?:"",
                    color=MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
            )
            }// fin state

            Spacer(modifier= Modifier.height(16.dp))

            Button(
                onClick = {
                    vm.submit { user, role ->
                        navController.navigate("DrawerMenu/$user/$role") {
                            popUpTo("login") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    if (state.isLoading) "Validando..."
                    else "Iniciar sesión"
                )
            }




        }//fin inner


    } // Fin Columna



    } // Final aplicacion Material

}// fin HomeScreen


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() { // Crear un navController de manera ficticia para fines de la vista previa
    val navController = rememberNavController()

    // Puedes usar un ViewModel simulado aquí si no tienes acceso a uno real
    val vm = LoginViewModel() // Suponiendo que LoginViewModel está correctamente configurado para la vista previa

    HomeScreen(navController = navController, vm = vm)
}
