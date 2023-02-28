package com.danntrp.movies.data.remote.model

data class MovieDescriptionDto(
    val kinopoiskId: Int,
    val imdbId: String?,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val posterUrl: String,
    val posterUrlPreview: String,
    val coverUrl: String?,
    val logoUrl: String?,
    val reviewsCount: Int,
    val ratingGoodReview: Double?,
    val ratingGoodReviewVoteCount: Int?,
    val ratingKinopoisk: Double?,
    val ratingKinopoiskVoteCount: Int?,
    val ratingImdb: Double?,
    val ratingImdbVoteCount: Int?,
    val ratingFilmCritics: Double?,
    val ratingFilmCriticsVoteCount: Int?,
    val ratingAwait: Double?,
    val ratingAwaitCount: Int?,
    val ratingRfCritics: Double?,
    val ratingRfCriticsVoteCount: Int?,
    val webUrl: String,
    val year: Int?,
    val filmLength: Int?,
    val slogan: String?,
    val description: String?,
    val shortDescription: String?,
    val editorAnnotation: String?,
    val isTicketsAvailable: Boolean,
    val productionStatus: String?,
    val type: String,
    val ratingMpaa: String?,
    val ratingAgeLimits: String?,
    val countries: List<CountryDto>,
    val genres: List<GenreDto>,
    val startYear: Int?,
    val endYear: Int?,
    val serial: Boolean?,
    val shortFilm: Boolean?,
    val completed: Boolean?,
    val hasImax: Boolean?,
    val has3D: Boolean?,
    val lastSync: String
) {
    interface Mapper<T> {
        fun map(movieDescriptionDto: MovieDescriptionDto): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(this)
}