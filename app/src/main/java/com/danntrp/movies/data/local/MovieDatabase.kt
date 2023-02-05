package com.danntrp.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.danntrp.movies.data.local.model.MovieDescriptionEntity
import com.danntrp.movies.data.local.model.MovieEntity

@Database(
    entities = [MovieEntity::class, MovieDescriptionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract val dao: MovieDao
}