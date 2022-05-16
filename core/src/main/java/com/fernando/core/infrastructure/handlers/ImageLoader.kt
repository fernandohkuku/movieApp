package com.fernando.core.infrastructure.handlers

import android.graphics.Bitmap

interface ImageLoader {
    suspend fun load(imageUrl: String): Bitmap?
}