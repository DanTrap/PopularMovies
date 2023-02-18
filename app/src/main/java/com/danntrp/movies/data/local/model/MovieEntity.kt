package com.danntrp.movies.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteMovies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val index: Int = 0,
    val id: Int,
    val name: String,
    val year: String,
    val genre: String,
    val posterUrlPreview: String,
)