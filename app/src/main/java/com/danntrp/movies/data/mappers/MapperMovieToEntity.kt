package com.danntrp.movies.data.mappers

import com.danntrp.movies.data.local.model.MovieEntity
import com.danntrp.movies.domain.model.Movie

class MapperMovieToEntity : Movie.Mapper<MovieEntity> {
    override fun map(movie: Movie): MovieEntity {
        return with(movie) {
            MovieEntity(
                id = id,
                name = name,
                year = year,
                genre = genre,
                posterUrlPreview = posterUrlPreview,
            )
        }
    }
}