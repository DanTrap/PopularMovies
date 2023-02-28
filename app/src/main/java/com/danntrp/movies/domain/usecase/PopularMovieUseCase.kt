package com.danntrp.movies.domain.usecase

import androidx.paging.PagingData
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class PopularMovieUseCase(
    private val movieRepository: MovieRepository
) {

    fun getPagedMovie(): Flow<PagingData<Movie>> = movieRepository.getPagedPopularMovies()
}