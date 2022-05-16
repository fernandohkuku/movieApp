package com.fernando.ui_ktx.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase

val Firebase.crashlytics: FirebaseCrashlytics
    get() = FirebaseCrashlytics.getInstance()
