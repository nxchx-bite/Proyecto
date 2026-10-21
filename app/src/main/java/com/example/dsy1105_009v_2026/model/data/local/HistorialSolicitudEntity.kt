package com.example.dsy1105_009v_2026.model.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "historial_solicitudes",
    foreignKeys = [
        ForeignKey(
            entity = SolicitudEntity::class,
            parentColumns = ["id"],
            childColumns = ["solicitudId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["solicitudId"])
    ]
)
data class HistorialSolicitudEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Solicitud a la que pertenece el cambio
    val solicitudId: Long,

    // Estado antes de la modificación
    val estadoAnterior: String,

    // Estado después de la modificación
    val estadoNuevo: String,

    // Acción efectuada
    val accion: String = "Cambio de estado",

    // Comentario descriptivo
    val comentario: String = "",

    // Perfil responsable del cambio
    val perfil: String = "ADMINISTRADOR",

    // Usuario ficticio que realizó el cambio
    val usuario: String = "Administrador",

    // Fecha y hora del cambio
    val fechaHora: String
)

