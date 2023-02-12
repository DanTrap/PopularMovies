package com.danntrp.movies.domain.usecase

import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.repository.MovieRepository

class FavoriteMovieUseCase(
    private val movieRepository: MovieRepository
) {

    suspend fun insert(movie: Movie) {
        movieRepository.insert(movie)
    }

    fun favorites() = movieRepository.getFavoriteMovies()

    suspend fun delete(id: Int) {
        movieRepository.deleteMovie(id)
    }
}