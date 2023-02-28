package com.danntrp.movies.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.danntrp.movies.util.Resource
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.model.MovieDescription
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getPagedPopularMovies(): Flow<PagingData<Movie>>

    suspend fun getMovieDescription(id: Int): Resource<MovieDescription>

    suspend fun insert(movie: Movie)

    fun getFavoriteMovies(): LiveData<List<Movie>>

    suspend fun deleteMovie(id: Int)

    fun getFavoriteMoviesByName(name: String): LiveData<List<Movie>>
}