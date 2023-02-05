package com.danntrp.movies.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val year: String,
    val genre: String,
    val posterUrlPreview: String,
)
