package com.fernando.core.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fernando.core.data.local.converters.Converters
import com.fernando.core.data.local.dao.MovieDao
import com.fernando.core.data.local.dao.RemoteKeysDao
import com.fernando.core.data.local.models.MovieModel
import com.fernando.core.data.local.models.RemoteKeys


@Database(entities = [MovieModel::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao():RemoteKeysDao
}