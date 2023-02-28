package com.danntrp.movies.data.mappers

import com.danntrp.movies.data.remote.model.MovieDescriptionDto
import com.danntrp.movies.domain.model.MovieDescription

class MapperMovieDescriptionDtoToDomain : MovieDescriptionDto.Mapper<MovieDescription> {
    override fun map(movieDescriptionDto: MovieDescriptionDto): MovieDescription {
        return with(movieDescriptionDto) {
            MovieDescription(
                name = nameRu ?: (nameEn ?: (nameOriginal ?: "Name is empty")),
                description = description ?: "Description is empty",
                genres = genres.map { it.genre },
                countries = countries.map { it.country },
                posterUrl = posterUrl,
            )
        }
    }
}