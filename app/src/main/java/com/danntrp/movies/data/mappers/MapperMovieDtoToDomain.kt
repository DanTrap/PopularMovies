package com.danntrp.movies.data.mappers

import com.danntrp.movies.data.remote.model.MovieDto
import com.danntrp.movies.domain.model.Movie

class MapperMovieDtoToDomain : MovieDto.Mapper<Movie> {
    override fun map(movieDto: MovieDto, isFavorite: Boolean): Movie {
        return with(movieDto) {
            Movie(
                id = filmId,
                name = nameRu ?: (movieDto.nameEn ?: "Name is empty"),
                year = year ?: "Year is empty",
                genre = genres[0].genre,
                posterUrlPreview = posterUrlPreview,
                isFavorite = isFavorite
            )
        }
    }
}