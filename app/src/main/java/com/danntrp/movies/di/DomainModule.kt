package com.danntrp.movies.di

import com.danntrp.movies.domain.repository.MovieRepository
import com.danntrp.movies.domain.usecase.MovieDescriptionUseCase
import com.danntrp.movies.domain.usecase.PopularMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun providePopularMovieUseCase(movieRepository: MovieRepository): PopularMovieUseCase {
        return PopularMovieUseCase(movieRepository = movieRepository)
    }

    @Provides
    @Singleton
    fun provideMovieDescriptionUseCase(movieRepository: MovieRepository): MovieDescriptionUseCase {
        return MovieDescriptionUseCase(movieRepository = movieRepository)
    }
}