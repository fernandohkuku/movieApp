package com.fernando.ui_ktx.content

import android.content.Intent


fun Intent.addSingleTopFlags() {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
}


fun Intent.allowImages() = setType("image/*")


fun Intent.putAllowMultiple() = putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

fun Intent.allowAction() = setAction(Intent.ACTION_GET_CONTENT)

