package com.fernando.core.domain.repositories

import androidx.paging.PagingData
import com.fernando.core.data.local.models.MovieModel
import com.fernando.core.data.models.MovieDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getRemoteMovies(scope: CoroutineScope): Flow<PagingData<MovieDto>>
    suspend fun getLocalMovies(scope: CoroutineScope): Flow<PagingData<MovieDto>>
}