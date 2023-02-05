package com.danntrp.movies.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteMoviesDescription")
data class MovieDescriptionEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val description: String,
    val genres: List<String>,
    val countries: List<String>,
    val posterUrl: String,
)