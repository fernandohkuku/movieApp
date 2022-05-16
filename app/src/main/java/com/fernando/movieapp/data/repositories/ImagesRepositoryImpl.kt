package com.fernando.movieapp.data.repositories

import android.net.Uri
import com.fernando.movieapp.domain.repositories.ImagesRepository
import javax.inject.Inject

class ImagesRepositoryImpl @Inject constructor(
    private val remoteDataSource: ImagesRemoteDataSource
) :ImagesRepository {
    override suspend fun saveImages(images:List<Uri>) = remoteDataSource.saveImages(images)
}

interface ImagesRemoteDataSource{
    suspend fun saveImages(images:List<Uri>)
}