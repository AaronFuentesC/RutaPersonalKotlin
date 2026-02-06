package com.example.rutapersonal.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import com.example.rutapersonal.model.Ubicacion

@Entity(tableName = "punto_interes")
data class PuntoInteres(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val nombre: String,
    @Embedded val ubicacion: Ubicacion,
    val direccion: String
)