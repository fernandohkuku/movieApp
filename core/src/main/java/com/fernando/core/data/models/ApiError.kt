package com.fernando.core.data.models

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName ("success") val success:Boolean,
    @SerializedName ("status_code") val statusCode:Int,
    @SerializedName ("status_message") val message:String
)
