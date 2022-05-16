package com.fernando.core.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fernando.core.data.exceptions.ApiException
import com.fernando.core.data.exceptions.NotInternetException
import com.fernando.core.data.local.AppDatabase
import com.fernando.core.data.local.dao.MovieDao
import com.fernando.core.data.mappers.toException
import com.fernando.core.data.mappers.toModel
import com.fernando.core.data.models.MovieDto
import com.fernando.core.data.remote.api.MovieService
import retrofit2.HttpException
import java.io.IOException

class MovieMediator(
    private val service: MovieService,
    private val db: AppDatabase
) : PagingSource<Int, MovieDto>() {
    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> =
        try {
            val nextPage = params.currentPage() ?: FIRST_PAGE

            val envelope = service.getDiscoverMovies(nextPage)
            db.movieDao().insertAll(envelope.results.map { it.toModel() })

            LoadResult.Page(
                envelope.results,
                prevKey = if (nextPage == FIRST_PAGE) null else nextPage - 1,
                nextKey = if (envelope.results.isEmpty()) null else nextPage + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }




    private fun LoadParams<Int>.currentPage(): Int? = when (this) {
        is LoadParams.Refresh -> FIRST_PAGE
        else -> key
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}