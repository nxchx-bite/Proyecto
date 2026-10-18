

package com.example.dsy1105_009v_2026.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsy1105_009v_2026.model.SolicitudErrores
import com.example.dsy1105_009v_2026.model.SolicitudUiState
import com.example.dsy1105_009v_2026.model.data.local.SolicitudDatabase
import com.example.dsy1105_009v_2026.model.data.local.SolicitudEntity
import com.example.dsy1105_009v_2026.repository.SolicitudRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.Flow


class SolicitudViewModel(application: Application) :
    AndroidViewModel(application) {

    // Conectar el ViewModel con Room
    private val database =
        SolicitudDatabase.getDatabase(application)

    private val repository =
        SolicitudRepository(database.solicitudDao())

    // Observar todas las solicitudes almacenadas en Room
    val solicitudes: StateFlow<List<SolicitudEntity>> =
        repository.obtenerSolicitudes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Estado del formulario
    private val _estado = MutableStateFlow(SolicitudUiState())

    val estado: StateFlow<SolicitudUiState> =
        _estado.asStateFlow()

    // Estado del guardado
    private val _guardando = MutableStateFlow(false)

    val guardando: StateFlow<Boolean> =
        _guardando.asStateFlow()

    // Mensaje en caso de error al guardar
    private val _errorGuardado = MutableStateFlow<String?>(null)

    val errorGuardado: StateFlow<String?> =
        _errorGuardado.asStateFlow()

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

    // Actualizar la evidencia
    fun onEvidenciaChange(uri: String?) {
        _estado.update {
            it.copy(evidenciaUri = uri)
        }
    }

    // Validar el formulario
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

    // Guardar solicitud en Room
    fun guardarSolicitud(onGuardado: (Long) -> Unit) {

        if (_guardando.value || !validarFormulario()) {
            return
        }

        _guardando.value = true
        _errorGuardado.value = null

        viewModelScope.launch {

            try {
                val datos = _estado.value

                // Generar fecha y hora de creacion
                val fecha = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    Locale.getDefault()
                ).format(Date())

                // Crear la entidad para Room
                val solicitud = SolicitudEntity(
                    titulo = datos.titulo.trim(),
                    descripcion = datos.descripcion.trim(),
                    categoria = datos.categoria,
                    estado = "Recibida",
                    fechaCreacion = fecha,
                    evidenciaUri = datos.evidenciaUri
                )

                // Guardar y obtener el ID generado
                val id = repository.guardarSolicitud(solicitud)

                onGuardado(id)

            } catch (e: Exception) {
                _errorGuardado.value =
                    "No se pudo guardar la solicitud"
            } finally {
                _guardando.value = false
            }
        }
    }

    // Limpiar formulario
    fun limpiarFormulario() {
        _estado.value = SolicitudUiState()
        _errorGuardado.value = null
    }

    // Obtener el detalle de una solicitud por su ID
    fun obtenerSolicitudPorId(
        id: Long
    ): Flow<SolicitudEntity?> {
        return repository.obtenerSolicitudPorId(id)
    }
}

