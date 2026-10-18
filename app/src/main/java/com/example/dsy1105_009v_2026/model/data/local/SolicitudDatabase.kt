package com.example.dsy1105_009v_2026.model.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SolicitudEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SolicitudDatabase : RoomDatabase() {

    abstract fun solicitudDao(): SolicitudDao

    companion object {

        @Volatile
        private var INSTANCE: SolicitudDatabase? = null

        fun getDatabase(context: Context): SolicitudDatabase {

            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    SolicitudDatabase::class.java,
                    "solicitud_database"
                ).build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}
