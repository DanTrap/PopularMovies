package com.danntrp.movies.di

import android.content.Context
import androidx.room.Room
import com.danntrp.movies.BuildConfig
import com.danntrp.movies.data.local.MovieDatabase
import com.danntrp.movies.data.local.model.MovieEntity
import com.danntrp.movies.data.mappers.MapperMovieDescriptionDtoToDomain
import com.danntrp.movies.data.mappers.MapperMovieDtoToDomain
import com.danntrp.movies.data.mappers.MapperMovieEntityToDomain
import com.danntrp.movies.data.mappers.MapperMovieToEntity
import com.danntrp.movies.data.remote.AuthInterceptor
import com.danntrp.movies.data.remote.MovieService
import com.danntrp.movies.data.remote.model.MovieDescriptionDto
import com.danntrp.movies.data.remote.model.MovieDto
import com.danntrp.movies.data.repository.MovieRepositoryImpl
import com.danntrp.movies.domain.model.Movie
import com.danntrp.movies.domain.model.MovieDescription
import com.danntrp.movies.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideMovieService(): MovieService {

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.API_KEY))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(MovieService.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideGifDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context, MovieDatabase::class.java, "database-movie"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieService: MovieService,
        movieDatabase: MovieDatabase,
        mapperMovieDtoToDomain: MovieDto.Mapper<Movie>,
        mapperMovieDescriptionDtoToDomain: MovieDescriptionDto.Mapper<MovieDescription>,
        mapperMovieEntityToDomain: MovieEntity.Mapper<Movie>,
        mapperMovieToEntity: Movie.Mapper<MovieEntity>
    ): MovieRepository {
        return MovieRepositoryImpl(
            movieService = movieService,
            movieDatabase = movieDatabase.dao,
            mapperMovieDtoToDomain = mapperMovieDtoToDomain,
            mapperMovieDescriptionDtoToDomain = mapperMovieDescriptionDtoToDomain,
            mapperMovieEntityToDomain = mapperMovieEntityToDomain,
            mapperMovieToEntity = mapperMovieToEntity
        )
    }

    @Provides
    @Singleton
    fun provideMapperDtoToDomain(): MovieDto.Mapper<Movie> {
        return MapperMovieDtoToDomain()
    }

    @Provides
    @Singleton
    fun provideMapperMovieDescriptionDtoToDomain(): MovieDescriptionDto.Mapper<MovieDescription> {
        return MapperMovieDescriptionDtoToDomain()
    }

    @Provides
    @Singleton
    fun provideMapperMovieEntityToDomain(): MovieEntity.Mapper<Movie> {
        return MapperMovieEntityToDomain()
    }

    @Provides
    @Singleton
    fun provideMapperMovieToEntity(): Movie.Mapper<MovieEntity> {
        return MapperMovieToEntity()
    }
}