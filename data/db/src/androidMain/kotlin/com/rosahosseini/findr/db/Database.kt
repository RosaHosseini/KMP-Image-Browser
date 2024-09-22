package com.rosahosseini.findr.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

internal fun getAppDatabaseBuilder(
    context: Context
): RoomDatabase.Builder<AppDatabase> {
    val dbFile = context.getDatabasePath(APP_DB_NAME)
    return Room.databaseBuilder<AppDatabase>(context, dbFile.absolutePath)
}
