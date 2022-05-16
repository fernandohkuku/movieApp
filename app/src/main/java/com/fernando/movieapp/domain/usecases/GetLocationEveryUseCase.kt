package com.fernando.movieapp.domain.usecases

import com.fernando.core.domain.usecases.base.UseCase
import com.fernando.movieapp.domain.entities.LocationEntity
import com.fernando.movieapp.domain.managers.LocationManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetLocationEveryUseCase @Inject constructor(
    private val manager: LocationManager,
    background: CoroutineDispatcher
) : UseCase<Flow<LocationEntity>, Void>(background) {
    override suspend fun run(input: Void?): Flow<LocationEntity> {
        manager.requestEnable()
        return manager.getLocationEveryTime()
    }
}