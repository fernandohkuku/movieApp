package com.fernando.core.infrastructure.handlers

import android.graphics.Bitmap
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FrescoImageLoader : ImageLoader {
    private val imagePipeline = Fresco.getImagePipeline()

    override suspend fun load(imageUrl: String): Bitmap? = suspendCoroutine { continuation ->
        val imageRequest = ImageRequest.fromUri(imageUrl)
        val dataSource = imagePipeline.fetchDecodedImage(imageRequest, null)

        dataSource.subscribe(object : BaseBitmapDataSubscriber() {
            override fun onNewResultImpl(bitmap: Bitmap?) {
                continuation.resume(bitmap)
            }

            override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage?>?>) {
                Timber.e(dataSource.failureCause)
                continuation.resume(null)
            }
        }, UiThreadImmediateExecutorService.getInstance())
    }
}