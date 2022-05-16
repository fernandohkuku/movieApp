package com.fernando.movieapp.domain.usecases

import com.fernando.core.domain.usecases.base.UseCase
import com.fernando.movieapp.domain.entities.LocationUserEntity
import com.fernando.movieapp.domain.repositories.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class GetLocationsUseCase @Inject constructor(
    private val repository: LocationRepository,
    background:CoroutineDispatcher
) :UseCase<List<LocationUserEntity>, Unit>(background){
    override suspend fun run(input: Unit?): List<LocationUserEntity> =
        repository.getLocations()
}