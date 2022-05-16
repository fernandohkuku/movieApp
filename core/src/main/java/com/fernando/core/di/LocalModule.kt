package com.fernando.core.di

import android.content.Context
import androidx.room.Room
import com.fernando.core.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) =
       Room.databaseBuilder(
           context,
           AppDatabase::class.java,
           "movies_db"
       ).build()

    @Provides
    @Singleton
    fun provideMovieDao(appDatabase: AppDatabase) = appDatabase.movieDao()

}