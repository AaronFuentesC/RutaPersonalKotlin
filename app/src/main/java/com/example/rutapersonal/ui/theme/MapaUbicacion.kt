package com.example.rutapersonal.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.rutapersonal.model.Ubicacion
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.CustomZoomButtonsController
@Composable
fun MapaUbicacion(
    ubicacionActual: Ubicacion?,
    puntosGuardados: List<Ubicacion>,
    context: Context
) {
    AndroidView(
        factory = { ctx ->
            Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid",
                Context.MODE_PRIVATE))
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                setMultiTouchControls(true)
                isClickable = true
                val centro = ubicacionActual?.let { GeoPoint(it.lat, it.lng) } ?: GeoPoint(0.0, 0.0)
                controller.setZoom(15.0)
                controller.setCenter(centro)
// Marcador para ubicación actual
                if (ubicacionActual != null) {
                    val marker = Marker(this)
                    marker.position = GeoPoint(ubicacionActual.lat, ubicacionActual.lng)
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = "Ubicación actual"
                    overlays.add(marker)
                }
// Marcadores para puntos guardados
                puntosGuardados.forEach { punto ->
                    val marker = Marker(this)
                    marker.position = GeoPoint(punto.lat, punto.lng)
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = "Punto guardado"
                    overlays.add(marker)
                    17
                }
            }
        }
    )
}