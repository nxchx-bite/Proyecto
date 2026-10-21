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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.example.dsy1105_009v_2026.model.EstadoSolicitud
import kotlinx.coroutines.flow.first

class SolicitudViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _estado = MutableStateFlow(SolicitudUiState())
    val estado = _estado.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando = _guardando.asStateFlow()

    private val _errorGuardado = MutableStateFlow<String?>(null)
    val errorGuardado = _errorGuardado.asStateFlow()

    private val database =
        SolicitudDatabase.getDatabase(application)

    private val repository =
        SolicitudRepository(database.solicitudDao())

    val solicitudes: StateFlow<List<SolicitudEntity>> =
        repository.obtenerSolicitudes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun cambiarEstado(
        id: Long,
        nuevoEstado: String,
        onResultado: (String?) -> Unit
    ) {
        val destino = EstadoSolicitud.desdeDescripcion(
            nuevoEstado
        )

        if (destino == null) {
            onResultado("El estado seleccionado no es válido")
            return
        }

        viewModelScope.launch {
            try {
                // Consultar el estado actual desde Room
                val solicitud = repository
                    .obtenerSolicitudPorId(id)
                    .first()

                if (solicitud == null) {
                    onResultado("No se encontró la solicitud")
                    return@launch
                }

                val actual = EstadoSolicitud.desdeDescripcion(
                    solicitud.estado
                )

                if (actual == null) {
                    onResultado("El estado actual no es válido")
                    return@launch
                }

                // Si no cambió, no necesitamos actualizar Room
                if (actual == destino) {
                    onResultado(null)
                    return@launch
                }

                // Validar la transición
                if (destino !in actual.siguientesEstados()) {
                    onResultado(
                        "No se permite pasar de " +
                                "${actual.descripcion} a " +
                                destino.descripcion
                    )
                    return@launch
                }

                // Guardar el cambio en Room
                val filas = repository.actualizarEstado(
                    id = id,
                    estadoAnterior = actual.descripcion,
                    nuevoEstado = destino.descripcion
                )

                if (filas > 0) {
                    onResultado(null)
                } else {
                    onResultado(
                        "El estado cambió mientras editabas. " +
                                "Vuelve a revisar la solicitud."
                    )
                }

            } catch (e: Exception) {
                onResultado("No se pudo cambiar el estado")
            }
        }
    }


    fun obtenerSolicitudPorId(
        id: Long
    ): kotlinx.coroutines.flow.Flow<SolicitudEntity?> {
        return repository.obtenerSolicitudPorId(id)
    }
    fun cambiarPrioridad(
        id: Long,
        nuevaPrioridad: String,
        onResultado: (String?) -> Unit
    ) {
        val prioridadesPermitidas =
            listOf("Baja", "Media", "Alta")

        if (nuevaPrioridad !in prioridadesPermitidas) {
            onResultado("La prioridad seleccionada no es válida")
            return
        }

        viewModelScope.launch {
            try {
                val filas =
                    repository.actualizarPrioridad(
                        id,
                        nuevaPrioridad
                    )

                if (filas > 0) {
                    onResultado(null)
                } else {
                    onResultado("No se encontró la solicitud")
                }
            } catch (e: Exception) {
                onResultado("No se pudo cambiar la prioridad")
            }
        }
    }



    fun validarEdicion(
        titulo: String,
        descripcion: String,
        categoria: String
    ): SolicitudErrores {

        return SolicitudErrores(
            titulo = if (titulo.isBlank()) {
                "El título es obligatorio"
            } else {
                null
            },
            descripcion = if (descripcion.isBlank()) {
                "La descripción es obligatoria"
            } else {
                null
            },
            categoria = if (categoria.isBlank()) {
                "Debes seleccionar una categoría"
            } else {
                null
            }
        )
    }

    fun actualizarSolicitud(
        id: Long,
        titulo: String,
        descripcion: String,
        categoria: String,
        onResultado: (String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val filas = repository.actualizarSolicitud(
                    id = id,
                    titulo = titulo.trim(),
                    descripcion = descripcion.trim(),
                    categoria = categoria
                )

                if (filas > 0) {
                    onResultado(null)
                } else {
                    onResultado(
                        "No se pudo actualizar. Comprueba que la solicitud exista y esté recibida."
                    )
                }
            } catch (e: Exception) {
                onResultado(
                    e.message ?: "Ocurrió un error al actualizar la solicitud"
                )
            }
        }
    }

    fun asignarTecnico(
        id: Long,
        responsable: String,
        onResultado: (String?) -> Unit
    ) {
        if (responsable.isBlank()) {
            onResultado("Ingresa el nombre del técnico")
            return
        }

        viewModelScope.launch {
            try {
                val filas =
                    repository.asignarResponsable(
                        id,
                        responsable.trim()
                    )

                if (filas > 0) {
                    onResultado(null)
                } else {
                    onResultado("No se encontró la solicitud")
                }
            } catch (e: Exception) {
                onResultado("No se pudo asignar el técnico")
            }
        }
    }
    fun guardarSolicitud(onGuardada: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                _guardando.value = true
                _errorGuardado.value = null

                val solicitud = SolicitudEntity(
                    titulo = _estado.value.titulo,
                    descripcion = _estado.value.descripcion,
                    categoria = _estado.value.categoria,
                    estado = "Recibida",
                    fechaCreacion = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm",
                        Locale.getDefault()
                    ).format(Date()),
                    evidenciaUri = _estado.value.evidenciaUri
                )


                val id = repository.guardarSolicitud(solicitud)

                onGuardada(id)
            } catch (e: Exception) {
                _errorGuardado.value =
                    e.message ?: "No se pudo guardar la solicitud"
            } finally {
                _guardando.value = false
            }
        }
    }
    fun onDescripcionChange(descripcion: String) {
        _estado.value = _estado.value.copy(
            descripcion = descripcion
        )
    }

    fun onCategoriaChange(categoria: String) {
        _estado.value = _estado.value.copy(
            categoria = categoria
        )
    }
    // Actualizar la evidencia fotografica del formulario
    fun onEvidenciaChange(uri: String?) {
        _estado.value = _estado.value.copy(
            evidenciaUri = uri
        )
    }


    fun validarFormulario(): Boolean {
        val actual = _estado.value

        val errores = actual.errores.copy(
            titulo = if (actual.titulo.isBlank()) {
                "El título es obligatorio"
            } else {
                null
            },
            descripcion = if (actual.descripcion.isBlank()) {
                "La descripción es obligatoria"
            } else {
                null
            },
            categoria = if (actual.categoria.isBlank()) {
                "Debes seleccionar una categoría"
            } else {
                null
            }
        )

        _estado.value = actual.copy(errores = errores)

        return errores.titulo == null &&
                errores.descripcion == null &&
                errores.categoria == null
    }


    fun onTituloChange(titulo: String) {
        _estado.value = _estado.value.copy(
            titulo = titulo
        )
    }
}