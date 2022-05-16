package com.fernando.movieapp.domain.managers

import com.fernando.movieapp.domain.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

internal interface LocationManager {
    suspend fun requestEnable()

    suspend fun getLatestLocation(): LocationEntity

    suspend fun getLocationEveryTime():Flow<LocationEntity>
}