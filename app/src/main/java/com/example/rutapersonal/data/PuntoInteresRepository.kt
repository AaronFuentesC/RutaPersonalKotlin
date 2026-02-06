package com.example.rutapersonal.data

import com.example.rutapersonal.model.PuntoInteres
class PuntoInteresRepository(private val dao: PuntoInteresDao) {
    fun obtenerTodos() = dao.obtenerTodos()
    suspend fun guardar(punto: PuntoInteres) = dao.insertar(punto)
}