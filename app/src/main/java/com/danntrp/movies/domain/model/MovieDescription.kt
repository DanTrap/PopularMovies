package com.danntrp.movies.domain.model

data class MovieDescription(
    val name: String,
    val description: String,
    val genres: List<String>,
    val countries: List<String>,
    val posterUrl: String,
)