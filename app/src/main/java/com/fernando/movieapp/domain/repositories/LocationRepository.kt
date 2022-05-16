package com.fernando.movieapp.domain.repositories

import com.fernando.movieapp.data.models.LocationUserDto
import com.fernando.movieapp.domain.entities.LocationUserEntity
import com.google.android.gms.maps.model.LatLng

internal interface LocationRepository {
    suspend fun saveLocation(location:LocationUserDto)
    suspend fun getLocations():List<LocationUserEntity>
}