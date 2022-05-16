package com.fernando.core.di

import com.fernando.core.data.local.source.MovieLocalDataSourceImpl
import com.fernando.core.data.remote.repositories.MovieLocalDataSource
import com.fernando.core.data.remote.repositories.MovieRemoteDataSource
import com.fernando.core.data.remote.repositories.MovieRepositoryImpl
import com.fernando.core.data.remote.source.MovieRemoteDataSourceImpl
import com.fernando.core.domain.repositories.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindMovieRepository(
        repositoryImpl: MovieRepositoryImpl
    ):MovieRepository

    @Binds
    internal abstract fun bindRemoteDataSource(
        remoteDataSourceImpl: MovieRemoteDataSourceImpl
    ):MovieRemoteDataSource

    @Binds
    internal abstract fun bindLocalDataSource(
        localDataSource: MovieLocalDataSourceImpl
    ):MovieLocalDataSource

}