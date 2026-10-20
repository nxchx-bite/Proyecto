package com.example.dsy1105_009v_2026.ui.theme.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dsy1105_009v_2026.repository.AuthRepository

class LoginViewModel (
    private val repo: AuthRepository= AuthRepository()
): ViewModel(){

   var uiState by mutableStateOf(LoginUiState())
       private set

    fun onUsernameChange(value:String){
        uiState=uiState.copy(username=value, error=null )
    }

    fun onPasswordChange(value:String){
        uiState=uiState.copy(password=value, error=null )
    }




    fun submit(onSuccess: (String, String) -> Unit) {
        uiState = uiState.copy(
            isLoading = true,
            error = null
        )

        val credential = repo.login(
            uiState.username.trim(),
            uiState.password
        )

        uiState = uiState.copy(isLoading = false)

        if (credential != null) {
            onSuccess(credential.username, credential.role)
        } else {
            uiState = uiState.copy(
                error = "Credenciales inválidas"
            )
        }
    }




}