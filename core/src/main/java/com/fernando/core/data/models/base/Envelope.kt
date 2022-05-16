package com.fernando.core.data.models.base

import com.google.gson.annotations.SerializedName

data class Envelope<T>(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("results") val results: List<T>
)