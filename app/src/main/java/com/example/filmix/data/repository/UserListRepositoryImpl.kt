package com.example.filmix.data.repository

import com.example.filmix.data.local.dao.FavoriteDao
import com.example.filmix.data.local.dao.WatchLaterDao
import com.example.filmix.data.local.dao.ViewedDao
import com.example.filmix.data.local.entities.*
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.repository.UserListRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserListRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val watchLaterDao: WatchLaterDao,
    private val viewedDao: ViewedDao
) : UserListRepository {

    private fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
    }

    override suspend fun addToFavorites(movie: MovieItem) {
        val userId = getCurrentUserId()
        val favorite = FavoriteEntity(
            id = "${userId}_${movie.id}".hashCode(),
            userId = userId,
            movieId = movie.id,
            title = movie.title,
            year = movie.year,
            posterPath = movie.posterPath,
            rating = movie.rating,
            backdropPath = movie.backdropPath,
            mediaType = movie.mediaType,
            popularity = movie.popularity
        )
        favoriteDao.insertFavorite(favorite)
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        val userId = getCurrentUserId()
        favoriteDao.deleteFavorite(userId, movieId)
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        val userId = getCurrentUserId()
        return favoriteDao.isFavorite(userId, movieId)
    }

    override suspend fun getFavorites(): List<MovieItem> {
        val userId = getCurrentUserId()
        return favoriteDao.getFavoritesByUser(userId).map { entity ->
            MovieItem(
                id = entity.movieId,
                title = entity.title,
                year = entity.year,
                posterPath = entity.posterPath,
                rating = entity.rating,
                backdropPath = entity.backdropPath,
                mediaType = entity.mediaType,
                popularity = entity.popularity
            )
        }
    }

    override fun getFavoritesFlow(): Flow<List<MovieItem>> {
        val userId = getCurrentUserId()
        return favoriteDao.getFavoritesByUserFlow(userId).map { entities ->
            entities.map { entity ->
                MovieItem(
                    id = entity.movieId,
                    title = entity.title,
                    year = entity.year,
                    posterPath = entity.posterPath,
                    rating = entity.rating,
                    backdropPath = entity.backdropPath,
                    mediaType = entity.mediaType,
                    popularity = entity.popularity
                )
            }
        }
    }

    override suspend fun addToWatchLater(movie: MovieItem) {
        val userId = getCurrentUserId()
        val watchLater = WatchLaterEntity(
            id = "${userId}_${movie.id}".hashCode(),
            userId = userId,
            movieId = movie.id,
            title = movie.title,
            year = movie.year,
            posterPath = movie.posterPath,
            rating = movie.rating,
            backdropPath = movie.backdropPath,
            mediaType = movie.mediaType,
            popularity = movie.popularity
        )
        watchLaterDao.insertWatchLater(watchLater)
    }

    override suspend fun removeFromWatchLater(movieId: Int) {
        val userId = getCurrentUserId()
        watchLaterDao.deleteWatchLater(userId, movieId)
    }

    override suspend fun isInWatchLater(movieId: Int): Boolean {
        val userId = getCurrentUserId()
        return watchLaterDao.isInWatchLater(userId, movieId)
    }

    override suspend fun getWatchLater(): List<MovieItem> {
        val userId = getCurrentUserId()
        return watchLaterDao.getWatchLaterByUser(userId).map { entity ->
            MovieItem(
                id = entity.movieId,
                title = entity.title,
                year = entity.year,
                posterPath = entity.posterPath,
                rating = entity.rating,
                backdropPath = entity.backdropPath,
                mediaType = entity.mediaType,
                popularity = entity.popularity
            )
        }
    }

    override fun getWatchLaterFlow(): Flow<List<MovieItem>> {
        val userId = getCurrentUserId()
        return watchLaterDao.getWatchLaterByUserFlow(userId).map { entities ->
            entities.map { entity ->
                MovieItem(
                    id = entity.movieId,
                    title = entity.title,
                    year = entity.year,
                    posterPath = entity.posterPath,
                    rating = entity.rating,
                    backdropPath = entity.backdropPath,
                    mediaType = entity.mediaType,
                    popularity = entity.popularity
                )
            }
        }
    }

    override suspend fun addToViewed(movie: MovieItem) {
        val userId = getCurrentUserId()
        val viewed = ViewedEntity(
            id = "${userId}_${movie.id}".hashCode(),
            userId = userId,
            movieId = movie.id,
            title = movie.title,
            year = movie.year,
            posterPath = movie.posterPath,
            rating = movie.rating,
            backdropPath = movie.backdropPath,
            mediaType = movie.mediaType,
            popularity = movie.popularity
        )
        viewedDao.insertViewed(viewed)
    }

    override suspend fun removeFromViewed(movieId: Int) {
        val userId = getCurrentUserId()
        viewedDao.deleteViewed(userId, movieId)
    }

    override suspend fun isViewed(movieId: Int): Boolean {
        val userId = getCurrentUserId()
        return viewedDao.isViewed(userId, movieId)
    }

    override suspend fun getViewed(): List<MovieItem> {
        val userId = getCurrentUserId()
        return viewedDao.getViewedByUser(userId).map { entity ->
            MovieItem(
                id = entity.movieId,
                title = entity.title,
                year = entity.year,
                posterPath = entity.posterPath,
                rating = entity.rating,
                backdropPath = entity.backdropPath,
                mediaType = entity.mediaType,
                popularity = entity.popularity
            )
        }
    }

    override fun getViewedFlow(): Flow<List<MovieItem>> {
        val userId = getCurrentUserId()
        return viewedDao.getViewedByUserFlow(userId).map { entities ->
            entities.map { entity ->
                MovieItem(
                    id = entity.movieId,
                    title = entity.title,
                    year = entity.year,
                    posterPath = entity.posterPath,
                    rating = entity.rating,
                    backdropPath = entity.backdropPath,
                    mediaType = entity.mediaType,
                    popularity = entity.popularity
                )
            }
        }
    }

    override suspend fun clearAllUserLists() {
        val userId = getCurrentUserId()
        favoriteDao.deleteAllUserFavorites(userId)
        watchLaterDao.deleteAllUserWatchLater(userId)
        viewedDao.deleteAllUserViewed(userId)
    }

    override suspend fun getFavoritesCount(): Int {
        val userId = getCurrentUserId()
        return favoriteDao.getFavoritesCount(userId)
    }

    override suspend fun getWatchLaterCount(): Int {
        val userId = getCurrentUserId()
        return watchLaterDao.getWatchLaterCount(userId)
    }

    override suspend fun getViewedCount(): Int {
        val userId = getCurrentUserId()
        return viewedDao.getViewedCount(userId)
    }
}
