package com.fernando.movieapp.domain.usecases

import com.fernando.core.domain.usecases.base.UseCase
import com.fernando.movieapp.domain.entities.LocationEntity
import com.fernando.movieapp.domain.managers.LocationManager
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class GetLocationUseCase @Inject constructor(
    private val manager: LocationManager,
    background: CoroutineDispatcher
) : UseCase<LocationEntity, Void>(background) {
    override suspend fun run(input: Void?): LocationEntity {
        manager.requestEnable()
        return manager.getLatestLocation()
    }
}
