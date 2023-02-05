package com.danntrp.movies.data.local

import androidx.room.*
import com.danntrp.movies.data.local.model.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieEntity: MovieEntity): Long

    @Query("SELECT * FROM favoriteMovies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("DELETE FROM favoriteMovies WHERE id = :id")
    suspend fun deleteMovie(id: Int)
}