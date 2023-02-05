package com.danntrp.movies.domain.repository

import com.danntrp.movies.core.util.Resource
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.model.MovieDescription

interface MovieRepository {

    suspend fun getPopularMovies(page: Int): Resource<List<Movie>>

    suspend fun getMovieDescription(id: Int): Resource<MovieDescription>
}