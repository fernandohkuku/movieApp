package com.fernando.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fernando.core.data.local.models.MovieModel


@Dao
interface MovieDao {
    @Query("Select * From MovieModel Order by id Desc")
    fun getMovies(): PagingSource<Int, MovieModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieModel>)

    @Query("DELETE FROM MovieModel")
    suspend fun clearMovies()

}