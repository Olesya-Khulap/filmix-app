package com.example.filmix.data.mapper

import com.example.filmix.data.local.entities.*
import com.example.filmix.domain.models.*

object EntityToDomainMapper {

    // MovieEntity -> MovieItem
    fun MovieEntity.toDomain(): MovieItem {
        return MovieItem(
            id = this.id,
            title = this.title,
            year = this.year,
            posterPath = this.posterPath,
            rating = this.rating,
            backdropPath = this.backdropPath,
            mediaType = this.mediaType,
            popularity = this.popularity
        )
    }

    // FavoriteEntity -> MovieItem
    fun FavoriteEntity.toDomain(): MovieItem {
        return MovieItem(
            id = this.movieId,
            title = this.title,
            year = this.year,
            posterPath = this.posterPath,
            rating = this.rating,
            backdropPath = this.backdropPath,
            mediaType = this.mediaType,
            popularity = this.popularity
        )
    }

    // WatchLaterEntity -> MovieItem
    fun WatchLaterEntity.toDomain(): MovieItem {
        return MovieItem(
            id = this.movieId,
            title = this.title,
            year = this.year,
            posterPath = this.posterPath,
            rating = this.rating,
            backdropPath = this.backdropPath,
            mediaType = this.mediaType,
            popularity = this.popularity
        )
    }

    // ViewedEntity -> MovieItem
    fun ViewedEntity.toDomain(): MovieItem {
        return MovieItem(
            id = this.movieId,
            title = this.title,
            year = this.year,
            posterPath = this.posterPath,
            rating = this.rating,
            backdropPath = this.backdropPath,
            mediaType = this.mediaType,
            popularity = this.popularity
        )
    }

    // GenreEntity -> Genre
    fun GenreEntity.toDomain(): Genre {
        return Genre(
            id = this.id,
            name = this.name
        )
    }
}
