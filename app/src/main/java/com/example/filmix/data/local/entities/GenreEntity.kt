package com.example.filmix.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val lastUpdated: Long = System.currentTimeMillis()
)
