package com.fernando.movieapp.data.models

import com.google.android.gms.maps.model.LatLng

data class LocationUserDto(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val date: Long? = null
) {
    val latLng: LatLng
        get() = LatLng(latitude!!, longitude!!)
}