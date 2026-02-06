package com.example.rutapersonal
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rutapersonal.ui.SensorViewModel
import com.example.rutapersonal.ui.UbicacionViewModel
import com.example.rutapersonal.ui.MapaUbicacion
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import org.jetbrains.annotations.Debug


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RutaPersonalApp()
        }
    }
}
@Composable
fun RutaPersonalApp() {

    val context = LocalContext.current
    val sensorVm: SensorViewModel = viewModel()
    val ubicacionVm: UbicacionViewModel = viewModel()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) ubicacionVm.obtenerUbicacionActual()
        }
    )
    var nombrePunto by remember { mutableStateOf("") }
    var mostrarListaPuntos by remember { mutableStateOf(false) }
    val ubicacionActual by ubicacionVm.ubicacion.collectAsState()
    val direccionActual by ubicacionVm.direccion.collectAsState()
    val puntosGuardados by ubicacionVm.puntosGuardados.collectAsState()
    LaunchedEffect(Unit) {
        sensorVm.iniciarSensor(context)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Aceleración X: ${sensorVm.aceleracionX.collectAsState().value}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }) {
            Text("Obtener ubicación")
        }
        Spacer(modifier = Modifier.height(16.dp))
        ubicacionVm.ubicacion.collectAsState().value?.let { ubicacionActual ->
            Text("Ubicación: ${ubicacionActual.lat}, ${ubicacionActual.lng}")
            Text("Dirección: ${ubicacionVm.direccion.collectAsState().value}")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nombrePunto,
                onValueChange = { nombrePunto = it },
                label = { Text("Nombre del punto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))


// dos botones en un row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (nombrePunto.isNotBlank()) {
                        ubicacionVm.guardarPunto(
                            nombre = nombrePunto,
                            ubicacion = ubicacionActual,
                        direccion = direccionActual
                        )
                        nombrePunto = ""
                    }
                }) {
                    Text("Guardar punto")
                }
                18
                Button(onClick = { mostrarListaPuntos = true }) {
                    Text("Ver puntos")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
// Convertir PuntoInteres a Ubicacion para el mapa
            val puntosComoUbicacion = ubicacionVm.puntosGuardados.collectAsState().value.map {
                it.ubicacion }
            MapaUbicacion(
                ubicacionActual = ubicacionActual,
                puntosGuardados = puntosComoUbicacion,
                context = LocalContext.current
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Puntos guardados:", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(puntosGuardados) { punto ->
                    Text(". ${punto.nombre} – ${punto.direccion}")
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
        }
    }
}

