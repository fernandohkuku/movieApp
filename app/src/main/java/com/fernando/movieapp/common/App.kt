package com.fernando.movieapp.common

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.fernando.core.presentation.components.util.TimberUtils
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
       super.onCreate()
        initLogger()
        initFresco()
    }

    private fun initLogger() = TimberUtils.init(true)

    private fun initFresco() {
        Fresco.initialize(
            this, ImagePipelineConfig
                .newBuilder(applicationContext)
                .setDownsampleEnabled(true)
                .build()
        )
    }

    override fun onLowMemory() {
        super.onLowMemory()
        val imagePipeline = Fresco.getImagePipeline()
        imagePipeline.clearMemoryCaches()
        imagePipeline.clearDiskCaches()
        imagePipeline.clearCaches()
    }

}