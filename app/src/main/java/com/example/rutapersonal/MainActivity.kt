package com.example.rutapersonal
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import com.example.rutapersonal.ui.UbicacionViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rutapersonal.ui.SensorViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.rutapersonal.ui.MapaUbicacion


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
    val sensorVm: SensorViewModel = viewModel()
    val ubicacionVm: UbicacionViewModel = viewModel()

    val context = LocalContext.current
    LaunchedEffect(Unit) { sensorVm.iniciarSensor(context) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) ubicacionVm.obtenerUbicacionActual()
        }
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Aceleración X: ${sensorVm.aceleracionX.collectAsState().value}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }) {
            Text("Obtener ubicación")
        }
        ubicacionVm.ubicacion.collectAsState().value?.let {
            Text("Ubicación: ${it.lat}, ${it.lng}")
            Text("Dirección: ${ubicacionVm.direccion.collectAsState().value}")
            Spacer(modifier = Modifier.height(16.dp))
            MapaUbicacion(it.lat, it.lng, LocalContext.current)
        }
    }
}

