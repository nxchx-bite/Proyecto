package com.example.dsy1105_009v_2026.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsy1105_009v_2026.model.UsuarioErrores
import com.example.dsy1105_009v_2026.model.UsuarioUiState
import com.example.dsy1105_009v_2026.model.data.local.UsuarioDatabase
import com.example.dsy1105_009v_2026.model.data.local.UsuarioEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Agregar
class UsuarioViewModel(app: Application) : AndroidViewModel(app) {

    // Agregar
    private val dao = UsuarioDatabase.getDatabase(app).usuarioDao()

    private val _estado = MutableStateFlow(UsuarioUiState())

    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()


    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onClaveChange(valor: String) {
        _estado.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = null)
            )
        }
    }

    fun onDireccionChange(valor: String) {
        _estado.update {
            it.copy(
                direccion = valor,
                errores = it.errores.copy(direccion = null)
            )
        }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update {
            it.copy(aceptaTerminos = valor)
        }
    }


    fun validarFormulario(): Boolean {

        val errores = UsuarioErrores(
            nombre = if (_estado.value.nombre.isBlank()) {
                "El nombre es obligatorio"
            } else {
                null
            },

            correo = if (_estado.value.correo.isBlank()) {
                "El correo es obligatorio"
            } else {
                null
            },

            clave = if (_estado.value.clave.isBlank()) {
                "La contraseña es obligatoria"
            } else {
                null
            },

            direccion = if (_estado.value.direccion.isBlank()) {
                "La dirección es obligatoria"
            } else {
                null
            }
        )

        _estado.update {
            it.copy(
                errores = errores
            )
        }

        return errores.nombre == null &&
                errores.correo == null &&
                errores.clave == null &&
                errores.direccion == null
    }

    // Agregar
    // Guarda en Room
    fun guardar(onListo: () -> Unit) {
        val a = _estado.value
        viewModelScope.launch {
            dao.insertar(
                UsuarioEntity(
                    nombre = a.nombre,
                    correo = a.correo,
                    clave = a.clave,
                    direccion = a.direccion
                )
            )
            onListo()
        }
    }
//Hasta aqui


}
