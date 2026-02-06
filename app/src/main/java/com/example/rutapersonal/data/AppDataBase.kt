package com.example.rutapersonal.data

import androidx.room.Database

import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.rutapersonal.model.PuntoInteres
@Database(
    entities = [PuntoInteres::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun puntoInteresDao(): PuntoInteresDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            12
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ruta_personal_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}