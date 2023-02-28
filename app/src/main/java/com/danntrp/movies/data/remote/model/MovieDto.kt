package com.danntrp.movies.data.remote.model

data class MovieDto(
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
) {
    interface Mapper<T> {
        fun map(movieDto: MovieDto, isFavorite: Boolean): T
    }

    fun <T> map(mapper: Mapper<T>, isFavorite: Boolean): T = mapper.map(this, isFavorite)
}