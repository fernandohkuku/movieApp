package com.fernando.movieapp.di

import com.fernando.core.infrastructure.error.ApiErrorHandler
import com.fernando.core.infrastructure.error.ErrorHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {
    @Named("ErrorHandler")
    @Binds
    abstract fun bindApiErrorHandler(apiErrorHandler: ApiErrorHandler): ErrorHandler
}