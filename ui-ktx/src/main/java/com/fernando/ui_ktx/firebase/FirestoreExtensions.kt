package com.fernando.ui_ktx.firebase

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await


suspend fun Query.getAsyncResult(): QuerySnapshot = get().await()


val QuerySnapshot.isNotEmpty: Boolean
    get() = !isEmpty
