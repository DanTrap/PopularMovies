package com.danntrp.movies.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.danntrp.movies.data.local.model.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieEntity: MovieEntity): Long

    @Query("SELECT * FROM favoriteMovies")
    fun getAllMoviesLive(): LiveData<List<MovieEntity>>

    @Query("DELETE FROM favoriteMovies WHERE id = :id")
    suspend fun deleteMovie(id: Int)
}