package com.example.filmix.data.local.dao

import androidx.room.*
import com.example.filmix.data.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // Movies
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)

    // Categories
    @Query("SELECT * FROM movies WHERE isPopular = 1 ORDER BY popularity DESC")
    suspend fun getPopularMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE isTopRated = 1 ORDER BY rating DESC")
    suspend fun getTopRatedMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE isNowPlaying = 1 ORDER BY lastUpdated DESC")
    suspend fun getNowPlayingMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE isBanner = 1 ORDER BY popularity DESC LIMIT 3")
    suspend fun getBannerMovies(): List<MovieEntity>

    // Search
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' ORDER BY popularity DESC")
    suspend fun searchMovies(query: String): List<MovieEntity>

    // Cleaning
    @Query("DELETE FROM movies WHERE lastUpdated < :timestamp")
    suspend fun deleteOldMovies(timestamp: Long)

    // Cloeaning by categories
    @Query("UPDATE movies SET isPopular = 0")
    suspend fun clearPopularMovies()

    @Query("UPDATE movies SET isTopRated = 0")
    suspend fun clearTopRatedMovies()

    @Query("UPDATE movies SET isNowPlaying = 0")
    suspend fun clearNowPlayingMovies()

    @Query("UPDATE movies SET isBanner = 0")
    suspend fun clearBannerMovies()

    @Query("SELECT * FROM movies ORDER BY popularity DESC LIMIT :limit OFFSET :offset")
    suspend fun getMoviesPaged(limit: Int, offset: Int): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE isPopular = 1 ORDER BY popularity DESC")
    fun getPopularMoviesFlow(): Flow<List<MovieEntity>>
}
