package com.danntrp.movies.data.repository

import com.danntrp.movies.core.util.Resource
import com.danntrp.movies.data.remote.MovieService
import com.danntrp.movies.data.remote.model.MovieDescriptionDto
import com.danntrp.movies.data.remote.model.MovieDto
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.model.MovieDescription
import com.danntrp.movies.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val movieService: MovieService
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Resource<List<Movie>> {
        val response = movieService.getPopularMovies(page = page)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!.films.map { it.toDomain() })
        } else {
            Resource.Error(response.message())
        }
    }

    override suspend fun getMovieDescription(id: Int): Resource<MovieDescription> {
        val response = movieService.getMovieDescriptionById(id)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!.toDomain())
        } else {
            Resource.Error(response.message())
        }
    }

    private fun MovieDto.toDomain(): Movie {
        return Movie(
            id = filmId,
            name = nameRu ?: (nameEn ?: "Name is empty"),
            year = year ?: "Year is empty",
            genre = genres[0].genre,
            posterUrlPreview = posterUrlPreview,
        )
    }

    private fun MovieDescriptionDto.toDomain(): MovieDescription {
        return MovieDescription(
            name = nameRu ?: (nameEn ?: "Name is empty"),
            description = description ?: "Description is empty",
            genres = genres.map { it.genre },
            countries = countries.map { it.country },
            posterUrl = posterUrl,
        )
    }
}