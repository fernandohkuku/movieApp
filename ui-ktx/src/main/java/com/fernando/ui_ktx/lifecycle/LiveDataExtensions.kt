package com.fernando.ui_ktx.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.fernando.ui_ktx.kotlin.isNotNull

fun <T> LiveData<T>.observeNewData(lifecycleOwner: LifecycleOwner, observer: (it: T) -> Unit) {
    var hasPreviousData = value.isNotNull()
    observe(lifecycleOwner) {
        if (!hasPreviousData) {
            observer(it)
        } else {
            hasPreviousData = false
        }
    }
}