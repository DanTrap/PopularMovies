package com.danntrp.movies.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val year: String,
    val genre: String,
    val posterUrlPreview: String,
    var isFavorite: Boolean = false,
) {
    interface Mapper<T> {
        fun map(movie: Movie): T
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(this)
}
