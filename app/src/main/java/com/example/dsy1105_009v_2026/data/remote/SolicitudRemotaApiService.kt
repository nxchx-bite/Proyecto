package com.example.dsy1105_009v_2026.data.remote

import com.example.dsy1105_009v_2026.model.ActualizarGestionRemotaRequest
import com.example.dsy1105_009v_2026.model.CrearSolicitudRemotaRequest
import com.example.dsy1105_009v_2026.model.SolicitudRemota

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SolicitudRemotaApiService {

    // GET: consultar solicitudes
    @GET("api/solicitudes")
    suspend fun obtenerSolicitudes(): List<SolicitudRemota>

    // POST: crear una solicitud
    @POST("api/solicitudes")
    suspend fun crearSolicitud(
        @Body datos: CrearSolicitudRemotaRequest
    ): SolicitudRemota

    // PUT: modificar estado, prioridad y técnico
    @PUT("api/solicitudes/{id}/gestion")
    suspend fun actualizarGestion(
        @Path("id") id: String,
        @Body datos: ActualizarGestionRemotaRequest
    ): SolicitudRemota
}
