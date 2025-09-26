package com.example.filmix.data.mapper

import com.example.filmix.data.local.entities.*
import com.example.filmix.domain.models.*

object DomainToEntityMapper {

    // MovieItem -> MovieEntity
    fun MovieItem.toEntity(
        isPopular: Boolean = false,
        isTopRated: Boolean = false,
        isNowPlaying: Boolean = false,
        isBanner: Boolean = false
    ): MovieEntity {
        return MovieEntity(
            id = this.id,
            title = this.title,
            year = this.year,
            posterPath = this.posterPath,
            rating = this.rating,
            backdropPath = this.backdropPath,
            mediaType = this.mediaType,
            popularity = this.popularity,
            overview = null,
            genreIds = null,
            releaseDate = null,
            isPopular = isPopular,
            isTopRated = isTopRated,
            isNowPlaying = isNowPlaying,
            isBanner = isBanner
        )
    }

    // MovieItem -> FavoriteEntity
    fun MovieItem.toFavoriteEntity(userId: String): FavoriteEntity {
        return FavoriteEntity(
            id = "${userId}_${this.id}".hashCode(),
            userId = userId,
            movieId = this.id,
            title = this.title,
            year = this.year,
            posterPath = this.posterPath,
            rating = this.rating,
            backdropPath = this.backdropPath,
            mediaType = this.mediaType,
            popularity = this.popularity
        )
    }

    // MovieItem -> WatchLaterEntity
    fun MovieItem.toWatchLaterEntity(userId: String): WatchLaterEntity {
        return WatchLaterEntity(
            id = "${userId}_${this.id}".hashCode(),
            userId = userId,
            movieId = this.id,
            title = this.title,
            year = this.year,
            posterPath = this.posterPath,
            rating = this.rating,
            backdropPath = this.backdropPath,
            mediaType = this.mediaType,
            popularity = this.popularity
        )
    }

    // MovieItem -> ViewedEntity
    fun MovieItem.toViewedEntity(userId: String): ViewedEntity {
        return ViewedEntity(
            id = "${userId}_${this.id}".hashCode(),
            userId = userId,
            movieId = this.id,
            title = this.title,
            year = this.year,
            posterPath = this.posterPath,
            rating = this.rating,
            backdropPath = this.backdropPath,
            mediaType = this.mediaType,
            popularity = this.popularity
        )
    }

    // Genre -> GenreEntity
    fun Genre.toEntity(type: String): GenreEntity {
        return GenreEntity(
            id = this.id,
            name = this.name,
            type = type
        )
    }
}
