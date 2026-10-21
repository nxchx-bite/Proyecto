package com.example.dsy1105_009v_2026.model

enum class EstadoSolicitud(val descripcion: String) {

    RECIBIDA("Recibida"),
    EN_REVISION("En revisión"),
    PENDIENTE_INFORMACION("Pendiente de información"),
    ASIGNADA("Asignada"),
    EN_PROCESO("En proceso"),
    RESUELTA("Resuelta"),
    CERRADA("Cerrada"),
    CANCELADA("Cancelada");

    fun siguientesEstados(): List<EstadoSolicitud> {
        return when (this) {

            RECIBIDA -> listOf(
                EN_REVISION,
                CANCELADA
            )

            EN_REVISION -> listOf(
                PENDIENTE_INFORMACION,
                ASIGNADA,
                CANCELADA
            )

            PENDIENTE_INFORMACION -> listOf(
                EN_REVISION,
                CANCELADA
            )

            ASIGNADA -> listOf(
                EN_PROCESO,
                CANCELADA
            )

            EN_PROCESO -> listOf(
                PENDIENTE_INFORMACION,
                RESUELTA
            )

            RESUELTA -> listOf(
                EN_PROCESO,
                CERRADA
            )

            CERRADA -> emptyList()

            CANCELADA -> emptyList()
        }
    }

    companion object {
        fun desdeDescripcion(
            descripcion: String
        ): EstadoSolicitud? {
            return entries.find {
                it.descripcion == descripcion
            }
        }
    }
}
