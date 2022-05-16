package com.fernando.movieapp.data.remote

import android.net.Uri
import com.fernando.movieapp.data.repositories.ImagesRemoteDataSource
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImagesRemoteDataSourceImpl @Inject constructor() : ImagesRemoteDataSource {

    private val storage: FirebaseStorage
        get() = Firebase.storage

    override suspend fun saveImages(images:List<Uri>) {
        images.forEach {uri ->
            storage.getReference("images/${uri.path}").putFile(uri).await()
        }
    }
}