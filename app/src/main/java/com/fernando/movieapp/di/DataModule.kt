package com.fernando.movieapp.di

import com.fernando.movieapp.data.remote.ImagesRemoteDataSourceImpl
import com.fernando.movieapp.data.remote.LocationRemoteDataSourceImpl
import com.fernando.movieapp.data.repositories.ImagesRemoteDataSource
import com.fernando.movieapp.data.repositories.ImagesRepositoryImpl
import com.fernando.movieapp.data.repositories.LocationRemoteDataSource
import com.fernando.movieapp.data.repositories.LocationRepositoryImpl
import com.fernando.movieapp.domain.repositories.ImagesRepository
import com.fernando.movieapp.domain.repositories.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract  class DataModule {
    @Binds
    internal abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ):LocationRepository

    @Binds
    internal abstract fun bindLocationRemoteDataSource(
        remoteDataSourceImpl: LocationRemoteDataSourceImpl
    ):LocationRemoteDataSource

    @Binds
    internal abstract fun bindImagesRepository(
        imagesRepositoryImpl: ImagesRepositoryImpl
    ):ImagesRepository

    @Binds
    internal abstract fun bindImagesRemoteDataSource(
        imagesRemoteDataSourceImpl: ImagesRemoteDataSourceImpl
    ):ImagesRemoteDataSource

//    companion object{
//        @Provides
//        @Singleton
//        fun provideLocationRemoteDataSourceImpl() = LocationRemoteDataSourceImpl()
//    }




}