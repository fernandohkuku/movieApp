package com.fernando.core.data.remote.source

import androidx.paging.*
import com.fernando.core.data.local.AppDatabase
import com.fernando.core.data.models.MovieDto
import com.fernando.core.data.remote.api.MovieService
import com.fernando.core.data.remote.paging.MovieMediator
import com.fernando.core.data.remote.repositories.MovieRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val service: MovieService,
    private val db: AppDatabase
) : MovieRemoteDataSource {

    override suspend fun getMovies(scope: CoroutineScope): Flow<PagingData<MovieDto>> =
        Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = false),
            pagingSourceFactory = { MovieMediator(service, db)}
        ).flow.cachedIn(scope)
}