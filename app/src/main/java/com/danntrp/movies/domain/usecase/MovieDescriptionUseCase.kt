package com.danntrp.movies.domain.usecase

import com.danntrp.movies.domain.repository.MovieRepository

class MovieDescriptionUseCase(
    private val movieRepository: MovieRepository
) {

    suspend fun getDescription(id: Int) =
        movieRepository.getMovieDescription(id)
}