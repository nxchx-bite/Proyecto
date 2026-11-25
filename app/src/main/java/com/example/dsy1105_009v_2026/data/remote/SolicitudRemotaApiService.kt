package com.example.dsy1105_009v_2026.data.remote

import com.example.dsy1105_009v_2026.model.CrearSolicitudRemotaRequest
import com.example.dsy1105_009v_2026.model.SolicitudRemota

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SolicitudRemotaApiService {

    // Consultar solicitudes
    @GET("api/solicitudes")
    suspend fun obtenerSolicitudes(): List<SolicitudRemota>

    // Registrar una solicitud
    @POST("api/solicitudes")
    suspend fun crearSolicitud(
        @Body datos: CrearSolicitudRemotaRequest
    ): SolicitudRemota
}
