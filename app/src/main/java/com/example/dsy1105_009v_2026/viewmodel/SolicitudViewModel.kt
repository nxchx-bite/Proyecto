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
import com.example.dsy1105_009v_2026.model.data.local.HistorialSolicitudEntity

import com.example.dsy1105_009v_2026.repository.SolicitudesRemotasRepository

class SolicitudViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _estado = MutableStateFlow(SolicitudUiState())
    val estado = _estado.asStateFlow()

    private val repositoryRemoto = SolicitudesRemotasRepository()

    private val _guardando = MutableStateFlow(false)
    val guardando = _guardando.asStateFlow()

    private val _errorGuardado = MutableStateFlow<String?>(null)
    val errorGuardado = _errorGuardado.asStateFlow()

    private val database =
        SolicitudDatabase.getDatabase(application)


    private val repository = SolicitudRepository(
        solicitudDao = database.solicitudDao(),
        historialDao = database.historialSolicitudDao()
    )


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
                // Consultar la solicitud desde Room
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

                // No registrar un cambio si el estado es igual
                if (actual == destino) {
                    onResultado(null)
                    return@launch
                }

                // Comprobar si la transición está permitida
                if (destino !in actual.siguientesEstados()) {
                    onResultado(
                        "No se permite pasar de " +
                                "${actual.descripcion} a ${destino.descripcion}"
                    )
                    return@launch
                }

                // Obtener la fecha y hora del cambio
                val fechaActual = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss",
                    Locale.getDefault()
                ).format(Date())

                // Crear el registro del historial
                val evento = HistorialSolicitudEntity(
                    solicitudId = id,
                    estadoAnterior = actual.descripcion,
                    estadoNuevo = destino.descripcion,
                    accion = "Cambio de estado",
                    comentario = "Se actualizó el estado de la solicitud.",
                    perfil = "ADMINISTRADOR",
                    usuario = "Administrador (simulado)",
                    fechaHora = fechaActual
                )

                // Actualizar estado y guardar historial juntos
                val guardado = repository.cambiarEstadoConHistorial(
                    id = id,
                    estadoAnterior = actual.descripcion,
                    estadoNuevo = destino.descripcion,
                    evento = evento
                )

                if (guardado) {
                    onResultado(null)
                } else {
                    onResultado(
                        "El estado cambió mientras editabas. " +
                                "Vuelve a revisar la solicitud."
                    )
                }

            } catch (e: Exception) {
                onResultado(
                    "No se pudo actualizar el estado ni registrar el historial"
                )
            }
        }
    }



    // Permite al técnico actualizar el estado de una solicitud asignada
    // y guardar el comentario dentro del historial en una sola transacción.
    fun cambiarEstadoTecnico(
        id: Long,
        nuevoEstado: String,
        comentario: String,
        username: String,
        onResultado: (String?) -> Unit
    ) {
        val destino = EstadoSolicitud.desdeDescripcion(nuevoEstado)

        if (destino == null) {
            onResultado("El estado seleccionado no es válido")
            return
        }

        if (comentario.isBlank()) {
            onResultado("Debes escribir un comentario sobre el trabajo realizado")
            return
        }

        if (username.isBlank()) {
            onResultado("No se pudo identificar al técnico")
            return
        }

        viewModelScope.launch {
            try {
                val solicitud = repository
                    .obtenerSolicitudPorId(id)
                    .first()

                if (solicitud == null) {
                    onResultado("No se encontró la solicitud")
                    return@launch
                }

                if (solicitud.responsable != username) {
                    onResultado("Esta solicitud no está asignada a tu usuario")
                    return@launch
                }

                val actual = EstadoSolicitud.desdeDescripcion(solicitud.estado)

                if (actual == null) {
                    onResultado("El estado actual de la solicitud no es válido")
                    return@launch
                }

                // El técnico no puede cancelar ni cerrar solicitudes.
                val estadosPermitidos = actual.siguientesEstados().filter {
                    it != EstadoSolicitud.CANCELADA &&
                            it != EstadoSolicitud.CERRADA
                }

                if (destino !in estadosPermitidos) {
                    onResultado(
                        "No se permite pasar de ${actual.descripcion} a ${destino.descripcion}"
                    )
                    return@launch
                }

                val fechaActual = SimpleDateFormat(
                    "dd/MM/yyyy HH:mm:ss",
                    Locale.getDefault()
                ).format(Date())

                val evento = HistorialSolicitudEntity(
                    solicitudId = id,
                    estadoAnterior = actual.descripcion,
                    estadoNuevo = destino.descripcion,
                    accion = "Trabajo técnico",
                    comentario = comentario.trim(),
                    perfil = "TECNICO",
                    usuario = username,
                    fechaHora = fechaActual
                )

                val guardado = repository.cambiarEstadoConHistorial(
                    id = id,
                    estadoAnterior = actual.descripcion,
                    estadoNuevo = destino.descripcion,
                    evento = evento
                )

                if (guardado) {
                    onResultado(null)
                } else {
                    onResultado("La solicitud cambió mientras la editabas. Vuelve a intentarlo.")
                }
            } catch (e: Exception) {
                onResultado(
                    e.message ?: "No se pudo actualizar el estado ni guardar el comentario"
                )
            }
        }
    }

    fun obtenerSolicitudPorId(
        id: Long
    ): kotlinx.coroutines.flow.Flow<SolicitudEntity?> {
        return repository.obtenerSolicitudPorId(id)
    }

    // Obtener solicitudes asignadas a un técnico
    fun obtenerSolicitudesPorTecnico(
        username: String
    ): kotlinx.coroutines.flow.Flow<List<SolicitudEntity>> {
        return repository.obtenerSolicitudesPorTecnico(username)
    }

    // Consultar el historial de una solicitud
    fun obtenerHistorial(
        solicitudId: Long
    ): kotlinx.coroutines.flow.Flow<List<HistorialSolicitudEntity>> {
        return repository.obtenerHistorial(solicitudId)
    }


    fun guardarCambiosAdministracion(
        id: Long,
        nuevaPrioridad: String,
        nuevoEstado: String,
        nuevoResponsable: String,
        onResultado: (String?) -> Unit
    ) {

        // Validar la prioridad seleccionada
        if (nuevaPrioridad !in listOf("Baja", "Media", "Alta")) {
            onResultado("La prioridad seleccionada no es válida")
            return
        }

        // Validar el estado seleccionado
        val destino = EstadoSolicitud.desdeDescripcion(nuevoEstado)

        if (destino == null) {
            onResultado("El estado seleccionado no es válido")
            return
        }

        viewModelScope.launch {

            var remotoGuardado = false

            try {
                // 1. Buscar la solicitud local
                val solicitud = repository
                    .obtenerSolicitudPorId(id)
                    .first()

                if (solicitud == null) {
                    onResultado("No se encontró la solicitud")
                    return@launch
                }

                // 2. Comprobar el estado anterior
                val actual = EstadoSolicitud.desdeDescripcion(
                    solicitud.estado
                )

                if (actual == null) {
                    onResultado("El estado actual no es válido")
                    return@launch
                }

                // 3. Validar la transición de estado
                if (
                    actual != destino &&
                    destino !in actual.siguientesEstados()
                ) {
                    onResultado(
                        "No se permite pasar de " +
                                "${actual.descripcion} a ${destino.descripcion}"
                    )
                    return@launch
                }

                // 4. Recuperar el ID remoto
                val mongoId = solicitud.mongoId

                if (mongoId.isNullOrBlank()) {
                    onResultado(
                        "Esta solicitud no está vinculada a MongoDB. " +
                                "Registra una solicitud nueva para probar el PUT."
                    )
                    return@launch
                }

                // 5. Preparar el nombre del técnico
                val responsable = nuevoResponsable
                    .trim()
                    .ifBlank { null }

                // 6. Comprobar si realmente hubo cambios
                if (
                    solicitud.prioridad == nuevaPrioridad &&
                    solicitud.estado == nuevoEstado &&
                    solicitud.responsable == responsable
                ) {
                    onResultado(null)
                    return@launch
                }

                // 7. Actualizar primero MongoDB
                repositoryRemoto.actualizarGestion(
                    mongoId = mongoId,
                    estado = nuevoEstado,
                    prioridad = nuevaPrioridad,
                    responsable = responsable
                )

                remotoGuardado = true

                // 8. Crear historial solamente si cambió el estado
                val evento = if (actual != destino) {

                    val fechaActual = SimpleDateFormat(
                        "dd/MM/yyyy HH:mm:ss",
                        Locale.getDefault()
                    ).format(Date())

                    HistorialSolicitudEntity(
                        solicitudId = id,
                        estadoAnterior = actual.descripcion,
                        estadoNuevo = destino.descripcion,
                        accion = "Cambio de estado",
                        comentario = "Se actualizó el estado de la solicitud.",
                        perfil = "ADMINISTRADOR",
                        usuario = "Administrador (simulado)",
                        fechaHora = fechaActual
                    )

                } else {
                    null
                }

                // 9. Actualizar Room y su historial
                val localGuardado =
                    repository.guardarGestionConHistorial(
                        id = id,
                        estadoAnterior = solicitud.estado,
                        estadoNuevo = nuevoEstado,
                        prioridad = nuevaPrioridad,
                        responsable = responsable,
                        evento = evento
                    )

                // 10. Comunicar el resultado a la pantalla
                if (localGuardado) {

                    onResultado(null)

                } else {

                    onResultado(
                        "MongoDB se actualizó, pero Room no pudo " +
                                "guardar los cambios. Revisa la sincronización."
                    )
                }

            } catch (e: Exception) {

                android.util.Log.e(
                    "AdminPUT",
                    "Error al guardar cambios administrativos",
                    e
                )

                if (remotoGuardado) {

                    onResultado(
                        "MongoDB se actualizó, pero Room falló."
                    )

                } else {

                    onResultado(
                        "No se pudieron guardar cambios en MongoDB: " +
                                (e.message ?: "Error de conexión")
                    )
                }
            }
        }
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

                // 1. Preparar los datos de la solicitud
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

                // 2. Guardar en MongoDB mediante POST
                val remota = repositoryRemoto.guardarEnMongoDB(
                    titulo = solicitud.titulo,
                    descripcion = solicitud.descripcion,
                    categoria = solicitud.categoria
                )

                // 3. Verificar que MongoDB devolvió su ID
                if (remota.id.isBlank()) {
                    throw IllegalStateException(
                        "MongoDB no devolvió el identificador"
                    )
                }

                // 4. Guardar en Room con el ID de MongoDB
                val idLocal = repository.guardarSolicitud(
                    solicitud.copy(
                        mongoId = remota.id
                    )
                )

                // 5. Mostrar la confirmación
                onGuardada(idLocal)

            } catch (e: Exception) {

                _errorGuardado.value =
                    e.message ?: "No se pudo guardar la solicitud"

            } finally {

                _guardando.value = false
            }
        }
    }



    // Eliminar solicitud desde la administración
    fun eliminarSolicitud(
        id: Long,
        onResultado: (String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val filas = repository.eliminarSolicitud(id)

                if (filas > 0) {
                    onResultado(null)
                } else {
                    onResultado("No se encontró la solicitud.")
                }

            } catch (e: Exception) {
                onResultado(
                    e.message ?: "No se pudo eliminar la solicitud."
                )
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


    fun limpiarFormulario() {
        _estado.value = SolicitudUiState()
    }


}
