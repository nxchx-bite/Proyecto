package com.example.dsy1105_009v_2026.repository

import com.example.dsy1105_009v_2026.model.data.local.SolicitudDao
import com.example.dsy1105_009v_2026.model.data.local.SolicitudEntity
import kotlinx.coroutines.flow.Flow

class SolicitudRepository(
    private val solicitudDao: SolicitudDao
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



    // Actualizar una solicitud en Room
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

}
