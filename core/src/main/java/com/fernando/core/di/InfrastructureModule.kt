package com.fernando.core.di

import com.fernando.core.infrastructure.handlers.FrescoImageLoader
import com.fernando.core.infrastructure.handlers.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class InfrastructureModule {

    @Binds
    abstract fun provideImageLoader(frescoImageLoader: FrescoImageLoader): ImageLoader
}