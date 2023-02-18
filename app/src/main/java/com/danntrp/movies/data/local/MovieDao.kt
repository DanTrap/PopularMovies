package com.danntrp.movies.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.danntrp.movies.data.local.model.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movieEntity: MovieEntity): Long

    @Query("SELECT * FROM favoriteMovies")
    fun getAllMoviesLive(): LiveData<List<MovieEntity>>

    @Query("DELETE FROM favoriteMovies WHERE id = :id")
    suspend fun deleteMovie(id: Int)

    @Query("SELECT EXISTS(SELECT * FROM favoriteMovies WHERE id = :id)")
    suspend fun contains(id: Int): Boolean

    @Query("SELECT * FROM favoriteMovies WHERE name LIKE :name || '%' OR name LIKE '% ' || :name || '%'")
    fun getMovieByName(name: String): LiveData<List<MovieEntity>>
}