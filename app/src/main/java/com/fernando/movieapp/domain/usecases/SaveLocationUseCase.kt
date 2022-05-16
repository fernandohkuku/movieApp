package com.fernando.movieapp.domain.usecases

import com.fernando.core.domain.usecases.base.UseCase
import com.fernando.movieapp.data.mappers.toDto
import com.fernando.movieapp.domain.entities.LocationUserEntity
import com.fernando.movieapp.domain.repositories.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class SaveLocationUseCase @Inject constructor(
    private val repository: LocationRepository,
    background:CoroutineDispatcher
) :UseCase<Unit, LocationUserEntity>(background){
    override suspend fun run(input: LocationUserEntity?) {
        requireNotNull(input){"location user can't be null"}
        repository.saveLocation(input.toDto())
    }

}