
package com.example.dsy1105_009v_2026.model.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SolicitudDao {

    // Guardar una solicitud
    @Insert
    suspend fun insertar(solicitud: SolicitudEntity): Long

    // Obtener todas las solicitudes registradas
    @Query("SELECT * FROM solicitudes ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<SolicitudEntity>>

    // Buscar una solicitud por su identificador
    @Query("SELECT * FROM solicitudes WHERE id = :id LIMIT 1")
    fun obtenerPorId(id: Long): Flow<SolicitudEntity?>


    // Actualizar datos de una solicitud recibida
    @Query("""
        UPDATE solicitudes
        SET titulo = :titulo,
            descripcion = :descripcion,
            categoria = :categoria
        WHERE id = :id AND estado = 'Recibida'
    """)
    suspend fun actualizarSolicitud(
        id: Long,
        titulo: String,
        descripcion: String,
        categoria: String
    ): Int

}
