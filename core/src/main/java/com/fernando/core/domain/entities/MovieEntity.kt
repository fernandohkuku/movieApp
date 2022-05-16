package com.fernando.core.domain.entities

data class MovieEntity(
    val adult: Boolean,
    val backdrop_path: String?=null,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String?=null,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) {
    val backdrop: String
        get() = "https://image.tmdb.org/t/p/w500$backdrop_path"

    val poster: String
        get() = "https://image.tmdb.org/t/p/w500$poster_path"
}
