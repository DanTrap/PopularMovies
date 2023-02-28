package com.danntrp.movies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.danntrp.movies.data.MoviePagingSource
import com.danntrp.movies.data.local.MovieDao
import com.danntrp.movies.data.local.model.MovieEntity
import com.danntrp.movies.data.remote.MovieService
import com.danntrp.movies.data.remote.model.MovieDescriptionDto
import com.danntrp.movies.data.remote.model.MovieDto
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.model.MovieDescription
import com.danntrp.movies.domain.repository.MovieRepository
import com.danntrp.movies.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val movieService: MovieService,
    private val movieDatabase: MovieDao
) : MovieRepository {

    override fun getPagedPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieService) }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override suspend fun getMovieDescription(id: Int): Resource<MovieDescription> {
        val response = movieService.getMovieDescriptionById(id)
        return if (response.isSuccessful) {
            Resource.Success(response.body()!!.toDomain())
        } else {
            Resource.Error(response.message())
        }
    }

    override suspend fun insert(movie: Movie) {
        if (!movieDatabase.contains(movie.id)) movieDatabase.insert(movie.toEntity())
    }

    override fun getFavoriteMovies(): LiveData<List<Movie>> {
        return Transformations.map(movieDatabase.getAllMoviesLive()) { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun deleteMovie(id: Int) {
        movieDatabase.deleteMovie(id)
    }

    override fun getFavoriteMoviesByName(name: String): LiveData<List<Movie>> {
        return Transformations.map(movieDatabase.getMovieByName(name)) { list ->
            list.map { it.toDomain() }
        }
    }

    private suspend fun MovieDto.toDomain(): Movie {
        return Movie(
            id = filmId,
            name = nameRu ?: (nameEn ?: "Name is empty"),
            year = year ?: "Year is empty",
            genre = genres[0].genre,
            posterUrlPreview = posterUrlPreview,
            isFavorite = movieDatabase.contains(filmId)
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

    private fun MovieEntity.toDomain(): Movie {
        return Movie(
            id = id,
            name = name,
            year = year,
            genre = genre,
            posterUrlPreview = posterUrlPreview,
            isFavorite = true
        )
    }

    private fun Movie.toEntity(): MovieEntity {
        return MovieEntity(
            id = id,
            name = name,
            year = year,
            genre = genre,
            posterUrlPreview = posterUrlPreview,
        )
    }
}