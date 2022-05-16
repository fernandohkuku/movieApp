package com.fernando.movieapp.data.mappers

import com.fernando.movieapp.data.models.LocationUserDto
import com.fernando.movieapp.domain.entities.LocationUserEntity


internal fun LocationUserDto.toEntity() = LocationUserEntity(
    latitude = latitude,
    longitude= longitude,
    date = date
)

internal fun LocationUserEntity.toDto() = LocationUserDto(
    latitude = latitude,
    longitude= longitude,
    date = date
)