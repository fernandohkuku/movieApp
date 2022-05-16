package com.fernando.movieapp.infrastructure.mappers

import android.location.Location
import com.fernando.movieapp.domain.entities.LocationEntity
import com.fernando.movieapp.infrastructure.exceptions.LocationException
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsStatusCodes

internal fun Location.toEntity() = LocationEntity(
    latitude = latitude,
    longitude = longitude,
    accuracy = accuracy
)

internal fun ApiException.toLocationException() = when (statusCode) {
    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
        val resolvable = this as ResolvableApiException
        LocationException(resolvable.resolution)
    }
    else -> LocationException()
}