package com.danntrp.movies.data.mappers

import com.danntrp.movies.data.local.model.MovieEntity
import com.danntrp.movies.domain.model.Movie

class MapperMovieEntityToDomain : MovieEntity.Mapper<Movie> {
    override fun map(movieEntity: MovieEntity): Movie {
        return with(movieEntity) {
            Movie(
                id = id,
                name = name,
                year = year,
                genre = genre,
                posterUrlPreview = posterUrlPreview,
                isFavorite = true
            )
        }
    }
}