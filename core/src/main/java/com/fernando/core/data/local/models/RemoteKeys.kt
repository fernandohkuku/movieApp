package com.fernando.core.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
