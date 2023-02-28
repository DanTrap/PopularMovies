package com.danntrp.movies.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.danntrp.movies.data.local.MovieDao
import com.danntrp.movies.data.local.model.MovieEntity
import com.danntrp.movies.data.remote.MoviePagingSource
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
    private val movieDatabase: MovieDao,
    private val mapperMovieDtoToDomain: MovieDto.Mapper<Movie>,
    private val mapperMovieDescriptionDtoToDomain: MovieDescriptionDto.Mapper<MovieDescription>,
    private val mapperMovieEntityToDomain: MovieEntity.Mapper<Movie>,
    private val mapperMovieToEntity: Movie.Mapper<MovieEntity>
) : MovieRepository {

    override fun getPagedPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 1,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(movieService) }
        ).flow.map { pagingData ->
            pagingData.map {
                it.map(
                    mapperMovieDtoToDomain,
                    movieDatabase.contains(it.filmId)
                )
            }
        }
    }

    override suspend fun getMovieDescription(id: Int): Resource<MovieDescription> {
        val response = movieService.getMovieDescriptionById(id)
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            Resource.Success(body.map(mapperMovieDescriptionDtoToDomain))
        } else {
            Resource.Error(response.message())
        }
    }

    override suspend fun insert(movie: Movie) {
        if (!movieDatabase.contains(movie.id)) movieDatabase.insert(movie.map(mapperMovieToEntity))
    }

    override fun getFavoriteMovies(): LiveData<List<Movie>> {
        return Transformations.map(movieDatabase.getAllMoviesLive()) { list ->
            list.map { it.map(mapperMovieEntityToDomain) }
        }
    }

    override suspend fun deleteMovie(id: Int) {
        movieDatabase.deleteMovie(id)
    }

    override fun getFavoriteMoviesByName(name: String): LiveData<List<Movie>> {
        return Transformations.map(movieDatabase.getMovieByName(name)) { list ->
            list.map { it.map(mapperMovieEntityToDomain) }
        }
    }
}