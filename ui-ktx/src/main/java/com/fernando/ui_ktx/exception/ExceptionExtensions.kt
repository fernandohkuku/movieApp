package com.fernando.ui_ktx.exception

import timber.log.Timber

val Exception.unhandled: Exception
    get() {
        Timber.e(this)
        return this
    }

inline fun <T> ignoreRuntimeExceptions(block: () -> T): T? =
    try {
        block()
    } catch (ex: RuntimeException) {
        Timber.e(ex)
        null
    }
