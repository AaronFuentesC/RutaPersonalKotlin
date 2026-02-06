package com.example.rutapersonal.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.location.Geocoder
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Ubicacion(val lat: Double, val lng: Double)
class UbicacionViewModel(application: Application) : AndroidViewModel(application) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val _ubicacion = MutableStateFlow<Ubicacion?>(null)
    val ubicacion: StateFlow<Ubicacion?> = _ubicacion
    private val _direccion = MutableStateFlow<String>("")
    val direccion: StateFlow<String> = _direccion

    fun obtenerDireccion(lat: Double, lng: Double) {
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val addresses = withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(lat, lng, 1)
                }
                _direccion.value = addresses?.firstOrNull()?.getAddressLine(0) ?: "Ubicación desconocida"
            } catch (e: Exception) {
                _direccion.value = "Error al obtener dirección"
            }
        }
    }
        fun obtenerUbicacionActual() {
            if (ContextCompat.checkSelfPermission(getApplication(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val ubi = Ubicacion(it.latitude, it.longitude)
                        _ubicacion.value = ubi
                        obtenerDireccion(ubi.lat, ubi.lng)
                    }
                }
            }
        }


}


