package com.example.rutapersonal.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SensorViewModel : ViewModel(), SensorEventListener {

 private val _aceleracionX = MutableStateFlow(0f)
 val aceleracionX: StateFlow<Float> = _aceleracionX

 private lateinit var sensorManager: SensorManager
 private var accelerometer: Sensor? = null

 fun iniciarSensor(context: Context) {
  sensorManager =
   context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

  accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

  accelerometer?.let {
   sensorManager.registerListener(
    this,
    it,
    SensorManager.SENSOR_DELAY_NORMAL
   )
  }
 }

 override fun onSensorChanged(event: SensorEvent) {
  _aceleracionX.value = event.values[0]
 }

 override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  // No necesitas implementar nada aquí
 }

 override fun onCleared() {
  super.onCleared()
  sensorManager.unregisterListener(this)
 }
}

