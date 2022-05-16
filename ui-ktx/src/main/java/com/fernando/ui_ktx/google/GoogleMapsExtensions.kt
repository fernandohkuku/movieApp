package com.fernando.ui_ktx.google

import android.annotation.SuppressLint
import com.fernando.ui_ktx.kotlin.then
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.ktx.addMarker

private var markers = mutableListOf<Marker>()
private var lastMarker: Marker? = null

@SuppressLint("MissingPermission")
fun GoogleMap.config(
    onClickMarker: (marker: Marker) -> Unit = {},
    ){
    isMyLocationEnabled = true
    uiSettings.isMyLocationButtonEnabled = true
    uiSettings.isCompassEnabled = false
    uiSettings.isRotateGesturesEnabled = false
    uiSettings.isTiltGesturesEnabled = false
    setMinZoomPreference(12f)
    setMaxZoomPreference(24f)

    setOnMarkerClickListener { marker ->
        onClickMarker(marker)
        true
    }
}

fun GoogleMap.addMarker(
    latLng: LatLng?,
    title: String,
    data: Any
) {
    addMarker {
        icon(icon)
        position(latLng!!)
        title(title)
    }.apply {
        addTag(data)
        addMarker(this)
    }


}

fun GoogleMap.moveCamera(latLng: LatLng) {
    CameraPosition.builder()
        .target(latLng)
        .zoom(17f)
        .bearing(3f)
        .tilt(30f)
        .build().also { camera ->
            animateCamera(camera)
        }
}

fun GoogleMap.animateCamera(camera: CameraPosition) {
    animateCamera(CameraUpdateFactory.newCameraPosition(camera), 1000, null)
}

fun Marker.addTag(data: Any) {
    tag = data
}


fun Marker.addMarker(marker: Marker) {
    markers.add(marker)
}

fun GoogleMap.remove() {
    clear()
    clearMarkers()
}

fun clearMarkers() {
    markers.clear()
    lastMarker = null
}

fun Marker.show() {
    showInfoWindow()
}
