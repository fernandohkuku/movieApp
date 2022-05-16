package com.fernando.core.di

import com.fernando.core.infrastructure.handlers.FrescoImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideIO() = Dispatchers.IO

    @Provides
    @Singleton
    fun provideFrescoImageLoader()= FrescoImageLoader()


}