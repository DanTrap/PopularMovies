package com.danntrp.movies.di

import android.content.Context
import androidx.room.Room
import com.danntrp.movies.BuildConfig
import com.danntrp.movies.data.local.MovieDatabase
import com.danntrp.movies.data.remote.AuthInterceptor
import com.danntrp.movies.data.remote.MovieService
import com.danntrp.movies.data.repository.MovieRepositoryImpl
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
    fun provideMovieRepository(movieService: MovieService, movieDatabase: MovieDatabase): MovieRepository {
        return MovieRepositoryImpl(movieService = movieService, movieDatabase = movieDatabase.dao)
    }
}