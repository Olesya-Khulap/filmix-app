package com.example.filmix.domain.repository

import com.example.filmix.domain.models.MovieItem
import kotlinx.coroutines.flow.Flow

interface UserListRepository {

    suspend fun addToFavorites(movie: MovieItem)
    suspend fun removeFromFavorites(movieId: Int)
    suspend fun isFavorite(movieId: Int): Boolean
    suspend fun getFavorites(): List<MovieItem>
    fun getFavoritesFlow(): Flow<List<MovieItem>>

    suspend fun addToWatchLater(movie: MovieItem)
    suspend fun removeFromWatchLater(movieId: Int)
    suspend fun isInWatchLater(movieId: Int): Boolean
    suspend fun getWatchLater(): List<MovieItem>
    fun getWatchLaterFlow(): Flow<List<MovieItem>>

    suspend fun addToViewed(movie: MovieItem)
    suspend fun removeFromViewed(movieId: Int)
    suspend fun isViewed(movieId: Int): Boolean
    suspend fun getViewed(): List<MovieItem>
    fun getViewedFlow(): Flow<List<MovieItem>>

    suspend fun clearAllUserLists()

    suspend fun getFavoritesCount(): Int
    suspend fun getWatchLaterCount(): Int
    suspend fun getViewedCount(): Int
}
