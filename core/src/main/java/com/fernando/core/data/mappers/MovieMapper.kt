package com.fernando.core.data.mappers

import androidx.paging.PagingData
import com.fernando.core.data.exceptions.ApiException
import com.fernando.core.data.exceptions.BadRequestException
import com.fernando.core.data.exceptions.NotAuthorizedException
import com.fernando.core.data.exceptions.NotFoundException
import com.fernando.core.data.local.models.MovieModel
import com.fernando.core.data.models.MovieDto
import com.fernando.core.domain.entities.MovieEntity
import com.fernando.ui_ktx.exception.unhandled


internal fun ApiException.toException() = when (this) {
    is BadRequestException -> BadRequestException(message = "Bad request")
    is NotFoundException -> NotFoundException(message= "Not Found")
    is NotAuthorizedException -> NotAuthorizedException()
    else -> unhandled
}

internal fun MovieDto.toModel() = MovieModel(
    id = id,
    adult = adult,
    backdrop_path = backdrop_path,
    original_language = original_language,
    original_title = original_title,
    overview = overview,
    popularity = popularity,
    poster_path = poster_path,
    release_date = release_date,
    title = title,
    video = video,
    vote_average = vote_average,
    vote_count = vote_count
)

internal fun MovieDto.toEntity() = MovieEntity(
    id = id,
    adult = adult,
    backdrop_path = backdrop_path,
    original_language = original_language,
    original_title = original_title,
    overview = overview,
    popularity = popularity,
    poster_path = poster_path,
    release_date = release_date,
    title = title,
    video = video,
    vote_average = vote_average,
    vote_count = vote_count
)

internal fun MovieModel.toDto() = MovieDto(
    id = id,
    adult = adult,
    backdrop_path = backdrop_path,
    original_language = original_language,
    original_title = original_title,
    overview = overview,
    popularity = popularity,
    poster_path = poster_path,
    release_date = release_date,
    title = title,
    video = video,
    vote_average = vote_average,
    vote_count = vote_count
)


