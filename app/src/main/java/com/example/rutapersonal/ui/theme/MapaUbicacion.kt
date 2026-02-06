package com.example.rutapersonal.ui

// ui/MapaComposable.kt
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.CustomZoomButtonsController
@Composable
fun MapaUbicacion(lat: Double, lng: Double, context: Context) {
    AndroidView(
        factory = { ctx ->
            Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid",
                Context.MODE_PRIVATE))
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)

                // FIXED LINE: Use the enum instead of a boolean
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                setMultiTouchControls(true) // Optional: allows pinch-to-zoom if buttons are hidden
                isClickable = true
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(lat, lng))
                val marker = Marker(this)
                marker.position = GeoPoint(lat, lng)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                overlays.add(marker)
            }
        }
    )
}