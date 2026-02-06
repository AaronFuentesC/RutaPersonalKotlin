package com.example.rutapersonal.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rutapersonal.data.AppDatabase
import com.example.rutapersonal.data.PuntoInteresRepository
import com.example.rutapersonal.model.PuntoInteres
import com.example.rutapersonal.model.Ubicacion
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
class UbicacionViewModel(application: Application) : AndroidViewModel(application) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val _ubicacion = MutableStateFlow<Ubicacion?>(null)
    val ubicacion: StateFlow<Ubicacion?> = _ubicacion
    private val _direccion = MutableStateFlow<String>("")
    val direccion: StateFlow<String> = _direccion
    // --- Room: Puntos de interés ---
    private val db by lazy { AppDatabase.getDatabase(getApplication()) }
    private val repository by lazy { PuntoInteresRepository(db.puntoInteresDao()) }
    private val _puntosGuardados = MutableStateFlow<List<PuntoInteres>>(emptyList())
    val puntosGuardados: StateFlow<List<PuntoInteres>> = _puntosGuardados
    init {
        viewModelScope.launch {
            repository.obtenerTodos().collect { puntos ->
                _puntosGuardados.value = puntos
            }
        }
    }
    fun obtenerUbicacionActual() {
        if (ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val ubi = Ubicacion(it.latitude, it.longitude)
                    _ubicacion.value = ubi
                    obtenerDireccion(ubi.lat, ubi.lng)
                }
            }
        }
    }
    private fun obtenerDireccion(lat: Double, lng: Double) {
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(lat, lng, 1)
                }
                val direccion = addresses?.firstOrNull()?.getAddressLine(0) ?: "Ubicación desconocida"
                _direccion.value = direccion
            } catch (e: Exception) {
                _direccion.value = "Error al obtener dirección"
            }
        }
    }
    fun guardarPunto(nombre: String, ubicacion: Ubicacion, direccion: String) {
        viewModelScope.launch {
            val punto = PuntoInteres(
                nombre = nombre,
                ubicacion = ubicacion,
                direccion = direccion
            )
            repository.guardar(punto)
        }
    }
}


