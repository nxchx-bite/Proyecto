package com.example.dsy1105_009v_2026.repository

import com.example.dsy1105_009v_2026.data.remote.SolicitudesRemotasRetrofit
import com.example.dsy1105_009v_2026.model.CrearSolicitudRemotaRequest
import com.example.dsy1105_009v_2026.model.SolicitudRemota

class SolicitudesRemotasRepository {

    // Consultar solicitudes
    suspend fun obtenerSolicitudes(): List<SolicitudRemota> {
        return SolicitudesRemotasRetrofit.api.obtenerSolicitudes()
    }

    // Registrar una solicitud en MongoDB
    suspend fun guardarEnMongoDB(
        titulo: String,
        descripcion: String,
        categoria: String
    ): SolicitudRemota {

        val solicitud = CrearSolicitudRemotaRequest(
            titulo = titulo,
            descripcion = descripcion,
            categoria = categoria
        )

        return SolicitudesRemotasRetrofit.api.crearSolicitud(
            solicitud
        )
    }
}

