package com.fernando.movieapp.domain.repositories

import android.net.Uri

interface ImagesRepository {
    suspend fun saveImages(images:List<Uri>)
}