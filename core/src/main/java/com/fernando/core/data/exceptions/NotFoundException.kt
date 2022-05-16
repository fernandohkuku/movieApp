package com.fernando.core.data.exceptions

import android.content.Context
import androidx.annotation.StringRes

class NotFoundException(override val message: String) : ApiException(message) {
    companion object {
        fun fromResource(context: Context, @StringRes resId: Int) = NotFoundException(context.getString(resId))
    }
}
