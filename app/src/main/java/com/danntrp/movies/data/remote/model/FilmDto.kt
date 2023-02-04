package com.danntrp.movies.data.remote.model

data class FilmDto(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val year: String?,
    val filmLength: String?,
    val countries: List<CountryDto>,
    val genres: List<GenreDto>,
    val rating: String?,
    val ratingVoteCount: Int?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val ratingChange: Any?
)