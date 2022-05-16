package com.fernando.core.data.remote.api

import com.fernando.core.data.models.MovieDto
import com.fernando.core.data.models.base.Envelope
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    suspend fun getDiscoverMovies(@Query("page") page:Int?): Envelope<MovieDto>
}