package com.rosahosseini.findr.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rosahosseini.findr.local.database.dao.PhotoDao
import com.rosahosseini.findr.local.database.dao.SearchDao
import com.rosahosseini.findr.local.database.dao.SearchHistoryDao
import com.rosahosseini.findr.local.database.entity.PhotoEntity
import com.rosahosseini.findr.local.database.entity.SearchEntity
import com.rosahosseini.findr.local.database.entity.SearchHistoryEntity

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
                "Findr.db"
            ).build()
        }
    }
}
