package com.fernando.movieapp.data.repositories

import com.fernando.core.data.models.MovieDto
import com.fernando.movieapp.data.mappers.toEntity

import com.fernando.movieapp.data.models.LocationUserDto
import com.fernando.movieapp.domain.entities.LocationUserEntity
import com.fernando.movieapp.domain.repositories.LocationRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

internal class LocationRepositoryImpl @Inject constructor(
    private val remoteDataSource: LocationRemoteDataSource
) : LocationRepository {
    override suspend fun saveLocation(locationUser: LocationUserDto) =
        remoteDataSource.saveLocation(locationUser)

    override suspend fun getLocations(): List<LocationUserEntity>  =
        remoteDataSource.getLocations().map { it.toEntity() }

}

internal interface LocationRemoteDataSource {
    suspend fun saveLocation(location: LocationUserDto)
    suspend fun getLocations():List<LocationUserDto>
}