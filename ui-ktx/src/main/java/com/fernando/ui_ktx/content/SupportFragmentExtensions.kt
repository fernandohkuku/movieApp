package com.fernando.ui_ktx.content

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun SupportMapFragment.createMap(): GoogleMap {
    return withContext(Dispatchers.Main) {
        awaitMap()
    }
}