package com.fernando.core.data.local.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fernando.core.data.local.dao.MovieDao
import com.fernando.core.data.local.models.MovieModel
import com.fernando.core.data.remote.repositories.MovieLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val dao: MovieDao
) : MovieLocalDataSource {
    override fun getLocalMovies(scope: CoroutineScope): Flow<PagingData<MovieModel>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { dao.getMovies() }
        ).flow.cachedIn(scope)
}