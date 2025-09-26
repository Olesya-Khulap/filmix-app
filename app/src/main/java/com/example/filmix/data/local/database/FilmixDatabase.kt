package com.example.filmix.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.filmix.data.local.dao.*
import com.example.filmix.data.local.entities.*

@Database(
    entities = [
        MovieEntity::class,
        FavoriteEntity::class,
        WatchLaterEntity::class,
        ViewedEntity::class,
        GenreEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FilmixDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun watchLaterDao(): WatchLaterDao
    abstract fun viewedDao(): ViewedDao
    abstract fun genreDao(): GenreDao

    companion object {
        @Volatile
        private var INSTANCE: FilmixDatabase? = null

        fun getDatabase(context: Context): FilmixDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FilmixDatabase::class.java,
                    "filmix_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
