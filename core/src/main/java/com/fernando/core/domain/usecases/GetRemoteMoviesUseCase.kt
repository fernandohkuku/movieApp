package com.fernando.core.domain.usecases

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.fernando.core.data.mappers.toEntity
import com.fernando.core.data.models.MovieDto
import com.fernando.core.domain.entities.MovieEntity
import com.fernando.core.domain.repositories.MovieRepository
import com.fernando.core.domain.usecases.base.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRemoteMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
    background: CoroutineDispatcher
) : UseCase<Flow<PagingData<MovieEntity>>, CoroutineScope>(background) {
    override suspend fun run(input: CoroutineScope?): Flow<PagingData<MovieEntity>> {
        requireNotNull(input) { "Corountines scope can't be null" }
        return  repository.getRemoteMovies(input).map { it.map { movie -> movie.toEntity() } }
    }
}