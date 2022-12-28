package com.rosahosseini.bleacher.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rosahosseini.bleacher.local.database.dao.PhotoDao
import com.rosahosseini.bleacher.local.database.dao.SearchDao
import com.rosahosseini.bleacher.local.database.dao.SearchHistoryDao
import com.rosahosseini.bleacher.local.database.entity.PhotoEntity
import com.rosahosseini.bleacher.local.database.entity.SearchEntity
import com.rosahosseini.bleacher.local.database.entity.SearchHistoryEntity

@Database(
    entities = [SearchEntity::class, PhotoEntity::class, SearchHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAO
    abstract fun searchDao(): SearchDao
    abstract fun photoDao(): PhotoDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "Bleacher.db"
            ).build()
        }
    }
}