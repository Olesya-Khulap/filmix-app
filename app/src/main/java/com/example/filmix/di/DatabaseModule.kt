package com.example.filmix.di

import android.content.Context
import androidx.room.Room
import com.example.filmix.data.local.database.FilmixDatabase
import com.example.filmix.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFilmixDatabase(@ApplicationContext context: Context): FilmixDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FilmixDatabase::class.java,
            "filmix_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMovieDao(database: FilmixDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    fun provideFavoriteDao(database: FilmixDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    fun provideWatchLaterDao(database: FilmixDatabase): WatchLaterDao {
        return database.watchLaterDao()
    }

    @Provides
    fun provideViewedDao(database: FilmixDatabase): ViewedDao {
        return database.viewedDao()
    }

    @Provides
    fun provideGenreDao(database: FilmixDatabase): GenreDao {
        return database.genreDao()
    }
}
