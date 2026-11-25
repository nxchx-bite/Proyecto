
package com.example.dsy1105_009v_2026.repository

import com.example.dsy1105_009v_2026.data.remote.SolicitudesRemotasRetrofit
import com.example.dsy1105_009v_2026.model.ActualizarGestionRemotaRequest
import com.example.dsy1105_009v_2026.model.CrearSolicitudRemotaRequest
import com.example.dsy1105_009v_2026.model.SolicitudRemota

class SolicitudesRemotasRepository {

    // Consultar solicitudes
    suspend fun obtenerSolicitudes(): List<SolicitudRemota> {
        return SolicitudesRemotasRetrofit.api.obtenerSolicitudes()
    }

    // Crear solicitud en MongoDB
    suspend fun guardarEnMongoDB(
        titulo: String,
        descripcion: String,
        categoria: String
    ): SolicitudRemota {

        val datos = CrearSolicitudRemotaRequest(
            titulo = titulo,
            descripcion = descripcion,
            categoria = categoria
        )

        return SolicitudesRemotasRetrofit.api.crearSolicitud(datos)
    }

    // Actualizar una solicitud en MongoDB
    suspend fun actualizarGestion(
        mongoId: String,
        estado: String,
        prioridad: String,
        responsable: String?
    ): SolicitudRemota {

        val datos = ActualizarGestionRemotaRequest(
            estado = estado,
            prioridad = prioridad,
            responsable = responsable
        )

        return SolicitudesRemotasRetrofit.api.actualizarGestion(
            id = mongoId,
            datos = datos
        )
    }
}


