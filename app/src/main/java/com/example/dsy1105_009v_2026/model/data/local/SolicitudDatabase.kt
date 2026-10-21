package com.example.dsy1105_009v_2026.model.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        SolicitudEntity::class,
        HistorialSolicitudEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SolicitudDatabase : RoomDatabase() {

    abstract fun solicitudDao(): SolicitudDao

    // DAO del historial
    abstract fun historialSolicitudDao(): HistorialSolicitudDao

    companion object {

        @Volatile
        private var INSTANCE: SolicitudDatabase? = null

        // MIGRACION EXISTENTE DE TU COMPAÑERO
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE solicitudes ADD COLUMN prioridad TEXT NOT NULL DEFAULT 'Media'"
                )

                db.execSQL(
                    "ALTER TABLE solicitudes ADD COLUMN responsable TEXT"
                )
            }
        }

        // NUEVA MIGRACION: TABLA DE HISTORIAL
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS historial_solicitudes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        solicitudId INTEGER NOT NULL,
                        estadoAnterior TEXT NOT NULL,
                        estadoNuevo TEXT NOT NULL,
                        accion TEXT NOT NULL,
                        comentario TEXT NOT NULL,
                        perfil TEXT NOT NULL,
                        usuario TEXT NOT NULL,
                        fechaHora TEXT NOT NULL,
                        FOREIGN KEY(solicitudId)
                            REFERENCES solicitudes(id)
                            ON UPDATE NO ACTION
                            ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS
                    index_historial_solicitudes_solicitudId
                    ON historial_solicitudes(solicitudId)
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): SolicitudDatabase {
            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    SolicitudDatabase::class.java,
                    "solicitud_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}

