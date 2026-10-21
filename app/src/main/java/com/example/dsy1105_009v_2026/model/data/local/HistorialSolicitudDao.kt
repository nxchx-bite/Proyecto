package com.example.dsy1105_009v_2026.model.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorialSolicitudDao {

    // Registrar un nuevo evento en el historial
    @Insert
    suspend fun insertar(
        historial: HistorialSolicitudEntity
    ): Long

    // Consultar historial de una solicitud
    @Query("""
        SELECT * FROM historial_solicitudes
        WHERE solicitudId = :solicitudId
        ORDER BY id DESC
    """)
    fun obtenerPorSolicitud(
        solicitudId: Long
    ): Flow<List<HistorialSolicitudEntity>>
}

