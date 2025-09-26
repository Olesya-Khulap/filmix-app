package com.example.filmix.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val year: String,
    val posterPath: String,
    val rating: Double,
    val backdropPath: String?,
    val mediaType: String?,
    val popularity: Double,
    val overview: String? = null,
    val genreIds: String? = null,
    val releaseDate: String? = null,
    val isPopular: Boolean = false,
    val isTopRated: Boolean = false,
    val isNowPlaying: Boolean = false,
    val isBanner: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
