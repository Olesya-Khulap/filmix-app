package com.example.filmix.data.local.dao

import androidx.room.*
import com.example.filmix.data.local.entities.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY addedAt DESC")
    suspend fun getFavoritesByUser(userId: String): List<FavoriteEntity>

    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY addedAt DESC")
    fun getFavoritesByUserFlow(userId: String): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND movieId = :movieId)")
    suspend fun isFavorite(userId: String, movieId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE userId = :userId AND movieId = :movieId")
    suspend fun deleteFavorite(userId: String, movieId: Int)

    @Query("DELETE FROM favorites WHERE userId = :userId")
    suspend fun deleteAllUserFavorites(userId: String)

    @Query("SELECT COUNT(*) FROM favorites WHERE userId = :userId")
    suspend fun getFavoritesCount(userId: String): Int
}
