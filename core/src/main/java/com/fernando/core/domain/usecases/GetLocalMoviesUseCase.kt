package com.fernando.core.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fernando.core.data.mappers.toEntity
import com.fernando.core.domain.entities.MovieEntity
import com.fernando.core.domain.repositories.MovieRepository
import com.fernando.core.domain.usecases.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocalMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
    background: CoroutineDispatcher
) : UseCase<Flow<PagingData<MovieEntity>>, CoroutineScope>(background) {
    override suspend fun run(input: CoroutineScope?): Flow<PagingData<MovieEntity>> {
        requireNotNull(input) { "Scope cant be null" }
        return repository.getLocalMovies(input)
            .map { paging -> paging.map { movie -> movie.toEntity() } }
    }

}