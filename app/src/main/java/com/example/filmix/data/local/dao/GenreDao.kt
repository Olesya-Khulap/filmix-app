package com.example.filmix.data.local.dao

import androidx.room.*
import com.example.filmix.data.local.entities.GenreEntity

@Dao
interface GenreDao {

    @Query("SELECT * FROM genres WHERE type = :type ORDER BY name ASC")
    suspend fun getGenresByType(type: String): List<GenreEntity>

    @Query("SELECT * FROM genres ORDER BY name ASC")
    suspend fun getAllGenres(): List<GenreEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreEntity>)

    @Query("DELETE FROM genres WHERE type = :type")
    suspend fun deleteGenresByType(type: String)

    @Query("DELETE FROM genres WHERE lastUpdated < :timestamp")
    suspend fun deleteOldGenres(timestamp: Long)
}
