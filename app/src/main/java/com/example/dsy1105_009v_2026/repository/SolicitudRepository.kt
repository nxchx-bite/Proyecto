
package com.example.dsy1105_009v_2026.repository

import com.example.dsy1105_009v_2026.model.data.local.SolicitudDao
import com.example.dsy1105_009v_2026.model.data.local.SolicitudEntity
import kotlinx.coroutines.flow.Flow
import com.example.dsy1105_009v_2026.model.data.local.HistorialSolicitudEntity
import com.example.dsy1105_009v_2026.model.data.local.HistorialSolicitudDao

class SolicitudRepository(
    private val solicitudDao: SolicitudDao,
    private val historialDao: HistorialSolicitudDao
) {


    // Guardar una solicitud en Room
    suspend fun guardarSolicitud(
        solicitud: SolicitudEntity
    ): Long {
        return solicitudDao.insertar(solicitud)
    }

    // Obtener todas las solicitudes
    fun obtenerSolicitudes(): Flow<List<SolicitudEntity>> {
        return solicitudDao.obtenerTodas()
    }

    // Buscar una solicitud por su ID
    fun obtenerSolicitudPorId(
        id: Long
    ): Flow<SolicitudEntity?> {
        return solicitudDao.obtenerPorId(id)
    }

    // Actualizar una solicitud recibida
    suspend fun actualizarSolicitud(
        id: Long,
        titulo: String,
        descripcion: String,
        categoria: String
    ): Int {
        return solicitudDao.actualizarSolicitud(
            id = id,
            titulo = titulo,
            descripcion = descripcion,
            categoria = categoria
        )
    }


    // Cambiar estado y registrar el historial
// utilizando una transacción de Room
    suspend fun cambiarEstadoConHistorial(
        id: Long,
        estadoAnterior: String,
        estadoNuevo: String,
        evento: HistorialSolicitudEntity
    ): Boolean {
        return solicitudDao.cambiarEstadoConHistorial(
            solicitudId = id,
            estadoAnterior = estadoAnterior,
            estadoNuevo = estadoNuevo,
            evento = evento
        )
    }


    // Cambiar el estado de una solicitud

    suspend fun actualizarEstado(
        id: Long,
        estadoAnterior: String,
        nuevoEstado: String
    ): Int {
        return solicitudDao.actualizarEstado(
            id = id,
            estadoAnterior = estadoAnterior,
            nuevoEstado = nuevoEstado
        )
    }


    // Cambiar la prioridad
    suspend fun actualizarPrioridad(
        id: Long,
        nuevaPrioridad: String
    ): Int {
        return solicitudDao.actualizarPrioridad(
            id = id,
            nuevaPrioridad = nuevaPrioridad
        )
    }

    // Asignar un técnico responsable
    suspend fun asignarResponsable(
        id: Long,
        responsable: String?
    ): Int {
        return solicitudDao.asignarResponsable(
            id = id,
            responsable = responsable
        )
    }

    // Obtener el historial de una solicitud desde Room
    fun obtenerHistorial(
        solicitudId: Long
    ): Flow<List<HistorialSolicitudEntity>> {
        return historialDao.obtenerPorSolicitud(solicitudId)
    }

}
