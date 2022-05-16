package com.fernando.core.di

import com.fernando.core.data.remote.api.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {
    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit) = retrofit.create(MovieService::class.java)

}