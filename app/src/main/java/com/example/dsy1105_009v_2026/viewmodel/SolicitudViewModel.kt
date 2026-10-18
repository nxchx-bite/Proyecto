
package com.example.dsy1105_009v_2026.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dsy1105_009v_2026.model.SolicitudErrores
import com.example.dsy1105_009v_2026.model.SolicitudUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SolicitudViewModel : ViewModel() {

    // Estado interno del formulario
    private val _estado = MutableStateFlow(SolicitudUiState())

    // Estado que observara la interfaz
    val estado: StateFlow<SolicitudUiState> =
        _estado.asStateFlow()

    // Actualizar el titulo
    fun onTituloChange(valor: String) {
        _estado.update {
            it.copy(
                titulo = valor,
                errores = it.errores.copy(titulo = null)
            )
        }
    }

    // Actualizar la descripcion
    fun onDescripcionChange(valor: String) {
        _estado.update {
            it.copy(
                descripcion = valor,
                errores = it.errores.copy(descripcion = null)
            )
        }
    }

    // Actualizar la categoria
    fun onCategoriaChange(valor: String) {
        _estado.update {
            it.copy(
                categoria = valor,
                errores = it.errores.copy(categoria = null)
            )
        }
    }

    // Actualizar la imagen seleccionada o capturada
    fun onEvidenciaChange(uri: String?) {
        _estado.update {
            it.copy(evidenciaUri = uri)
        }
    }

    // Validar los datos antes de registrar
    fun validarFormulario(): Boolean {

        val datos = _estado.value

        val errores = SolicitudErrores(
            titulo = when {
                datos.titulo.isBlank() ->
                    "El titulo es obligatorio"
                datos.titulo.trim().length < 5 ->
                    "El titulo debe tener al menos 5 caracteres"
                else -> null
            },

            descripcion = when {
                datos.descripcion.isBlank() ->
                    "La descripcion es obligatoria"
                datos.descripcion.trim().length < 10 ->
                    "La descripcion debe tener al menos 10 caracteres"
                else -> null
            },

            categoria = if (datos.categoria.isBlank()) {
                "Debes seleccionar una categoria"
            } else {
                null
            }
        )

        _estado.update {
            it.copy(errores = errores)
        }

        return errores.titulo == null &&
                errores.descripcion == null &&
                errores.categoria == null
    }

    // Limpiar el formulario
    fun limpiarFormulario() {
        _estado.value = SolicitudUiState()
    }
}


