package com.rosahosseini.findr.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.rosahosseini.findr.db.dao.PhotoDao
import com.rosahosseini.findr.db.dao.SearchHistoryDao
import com.rosahosseini.findr.db.entity.PhotoEntity
import com.rosahosseini.findr.db.entity.SearchHistoryEntity

internal const val APP_DB_NAME = "Findr.db"

@Database(
    entities = [PhotoEntity::class, SearchHistoryEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    // DAO
    abstract fun photoDao(): PhotoDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
