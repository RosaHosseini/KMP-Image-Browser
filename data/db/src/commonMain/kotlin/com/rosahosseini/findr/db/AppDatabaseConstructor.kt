package com.rosahosseini.findr.db

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.com.rosahosseini.findr.library.coroutines.CoroutineDispatchers

internal fun createAppDatabase(
    builder: RoomDatabase.Builder<AppDatabase>,
    dispatchers: CoroutineDispatchers
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(dispatchers.io)
        .build()
}
