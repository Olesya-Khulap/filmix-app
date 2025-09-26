package com.example.filmix.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_later")
data class WatchLaterEntity(
    @PrimaryKey val id: Int,
    val userId: String,
    val movieId: Int,
    val title: String,
    val year: String,
    val posterPath: String,
    val rating: Double,
    val backdropPath: String?,
    val mediaType: String?,
    val popularity: Double,
    val addedAt: Long = System.currentTimeMillis()
)
