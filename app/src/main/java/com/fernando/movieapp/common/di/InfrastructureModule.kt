package com.fernando.movieapp.common.di

import android.app.Activity
import com.fernando.core.domain.managers.NotificationsManager
import com.fernando.core.infrastructure.managers.notification.NotificationsManagerImpl
import com.fernando.movieapp.HomeActivity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class InfrastructureModule {

    @Binds
    abstract fun provideNotificationManager(
        notificationsManagerImpl: NotificationsManagerImpl
    ): NotificationsManager

}

