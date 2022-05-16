package com.fernando.core.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MovieModel(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String?=null,
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
)
