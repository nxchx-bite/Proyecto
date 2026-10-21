
package com.example.dsy1105_009v_2026.model.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

@Database(
    entities = [SolicitudEntity::class],
    version = 2,
    exportSchema = false
)
abstract class SolicitudDatabase : RoomDatabase() {

    abstract fun solicitudDao(): SolicitudDao

    companion object {

        @Volatile
        private var INSTANCE: SolicitudDatabase? = null

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

        fun getDatabase(context: Context): SolicitudDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    SolicitudDatabase::class.java,
                    "solicitud_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}
