package com.danntrp.movies.data.remote.model

data class MoviesResponseDto(
    val pagesCount: Int,
    val films: List<FilmDto>
)