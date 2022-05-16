package com.fernando.movieapp.domain.entities

import com.google.android.gms.maps.model.LatLng


internal data class LocationEntity(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float = 0f
) {
    val latLng: LatLng
        get() = LatLng(latitude, longitude)
}
