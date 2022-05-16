package com.fernando.movieapp.presentation.components.contracts

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.fernando.ui_ktx.content.allowAction
import com.fernando.ui_ktx.content.allowImages
import com.fernando.ui_ktx.content.putAllowMultiple
import com.fernando.ui_ktx.kotlin.isNotNull
import timber.log.Timber
import java.lang.NullPointerException
import java.nio.file.Path

class GaleryContract : ActivityResultContract<Intent, Intent>() {
    private object Key {
        const val NAME = "authAccount"
    }

    override fun createIntent(context: Context, input: Intent?): Intent {
        input?.allowImages()
        input?.allowAction()
        input?.putAllowMultiple()
        return input!!
    }


    override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
        return try {
            intent
        } catch (e: NullPointerException) {
            Timber.e(e)
            null
        }
    }


    data class Image(
        private val path:String
    )

}