package com.fernando.core.data.remote.repositories

import androidx.paging.PagingData
import androidx.paging.map
import com.fernando.core.data.exceptions.NotInternetException
import com.fernando.core.data.local.models.MovieModel
import com.fernando.core.data.mappers.toDto
import com.fernando.core.data.mappers.toModel
import com.fernando.core.data.models.MovieDto
import com.fernando.core.domain.repositories.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localRemoteDataSource: MovieLocalDataSource
) : MovieRepository {

    override suspend fun getRemoteMovies(scope: CoroutineScope): Flow<PagingData<MovieDto>>  =
        remoteDataSource.getMovies(scope)


    override suspend fun getLocalMovies(scope: CoroutineScope): Flow<PagingData<MovieDto>> =
        localRemoteDataSource.getLocalMovies(scope).map { it.map {movie -> movie.toDto() } }

}

interface MovieRemoteDataSource {
    suspend fun getMovies(scope: CoroutineScope): Flow<PagingData<MovieDto>>
}

interface MovieLocalDataSource {
    fun getLocalMovies(scope: CoroutineScope): Flow<PagingData<MovieModel>>
}