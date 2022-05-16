package com.fernando.movieapp.domain.entities

import android.annotation.SuppressLint
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*

data class LocationUserEntity(
    val latitude:Double?=null,
    val longitude:Double?=null,
    val date: Long? = null,

){
    val dateAsString:String
    @SuppressLint("SimpleDateFormat")
    get() {
        val format = SimpleDateFormat("dd/MM/yy hh:mm a")
        val date = Date(date!!)
        return format.format(date)
    }

    val latLng:LatLng
        get() = LatLng(latitude!!, longitude!!)
}