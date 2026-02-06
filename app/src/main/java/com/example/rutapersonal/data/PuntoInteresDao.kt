package com.example.rutapersonal.data
import androidx.room.*
import com.example.rutapersonal.model.PuntoInteres
import kotlinx.coroutines.flow.Flow
@Dao
interface PuntoInteresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(punto: PuntoInteres)
    @Query("SELECT * FROM punto_interes ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<PuntoInteres>>
    @Delete
    suspend fun eliminar(punto: PuntoInteres)
}