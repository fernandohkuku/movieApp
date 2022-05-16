package com.fernando.movieapp.di

import android.content.Context
import com.fernando.movieapp.domain.managers.LocationManager
import com.fernando.movieapp.infrastructure.managers.LocationManagerImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class InfrastructureModule {
    @Binds
    internal abstract fun provideLocationManager(locationManagerImpl: LocationManagerImpl): LocationManager
}

