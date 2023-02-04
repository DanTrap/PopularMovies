package com.danntrp.movies.data.remote

import com.danntrp.movies.data.remote.model.MovieDescriptionDto
import com.danntrp.movies.data.remote.model.MoviesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("api/v2.2/films/top")
    suspend fun getPopularMovies(
        @Query("type") type: String = "TOP_100_POPULAR_FILMS",
        @Query("page") page: Int = 1
    ): Response<MoviesResponseDto>

    @GET("/api/v2.2/films/{id}")
    suspend fun getMovieDescriptionById(
        @Path("id") id: Int
    ): Response<MovieDescriptionDto>

    companion object {
        const val BASE_URL = "https://kinopoiskapiunofficial.tech/"
    }
}