package com.example.filmix.data.local.dao

import androidx.room.*
import com.example.filmix.data.local.entities.ViewedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ViewedDao {

    @Query("SELECT * FROM viewed WHERE userId = :userId ORDER BY viewedAt DESC")
    suspend fun getViewedByUser(userId: String): List<ViewedEntity>

    @Query("SELECT * FROM viewed WHERE userId = :userId ORDER BY viewedAt DESC")
    fun getViewedByUserFlow(userId: String): Flow<List<ViewedEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM viewed WHERE userId = :userId AND movieId = :movieId)")
    suspend fun isViewed(userId: String, movieId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViewed(viewed: ViewedEntity)

    @Query("DELETE FROM viewed WHERE userId = :userId AND movieId = :movieId")
    suspend fun deleteViewed(userId: String, movieId: Int)

    @Query("DELETE FROM viewed WHERE userId = :userId")
    suspend fun deleteAllUserViewed(userId: String)

    @Query("SELECT COUNT(*) FROM viewed WHERE userId = :userId")
    suspend fun getViewedCount(userId: String): Int
}
