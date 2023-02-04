package com.danntrp.movies.domain.usecase

import com.danntrp.movies.core.util.Resource
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.repository.MovieRepository

class PopularMovieUseCase(
    private val movieRepository: MovieRepository
) {

    suspend fun getMovies(page: Int): Resource<List<Movie>> =
        movieRepository.getPopularMovies(page)
}