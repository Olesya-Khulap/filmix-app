package com.example.filmix.data.local.dao

import androidx.room.*
import com.example.filmix.data.local.entities.WatchLaterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchLaterDao {

    @Query("SELECT * FROM watch_later WHERE userId = :userId ORDER BY addedAt DESC")
    suspend fun getWatchLaterByUser(userId: String): List<WatchLaterEntity>

    @Query("SELECT * FROM watch_later WHERE userId = :userId ORDER BY addedAt DESC")
    fun getWatchLaterByUserFlow(userId: String): Flow<List<WatchLaterEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watch_later WHERE userId = :userId AND movieId = :movieId)")
    suspend fun isInWatchLater(userId: String, movieId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchLater(watchLater: WatchLaterEntity)

    @Query("DELETE FROM watch_later WHERE userId = :userId AND movieId = :movieId")
    suspend fun deleteWatchLater(userId: String, movieId: Int)

    @Query("DELETE FROM watch_later WHERE userId = :userId")
    suspend fun deleteAllUserWatchLater(userId: String)

    @Query("SELECT COUNT(*) FROM watch_later WHERE userId = :userId")
    suspend fun getWatchLaterCount(userId: String): Int
}
