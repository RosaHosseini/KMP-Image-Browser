package com.rosahosseini.bleacher.search.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchWorkManagerScheduler @Inject constructor(private val context: Context) {

    fun scheduleClearCachePeriodically() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val worker = PeriodicWorkRequest.Builder(
            ClearCacheWorker::class.java,
            REPEAT_INTERVAL_DAYS,
            TimeUnit.DAYS
        ).addTag(TAG_CLEAR_CACHE)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            TAG_CLEAR_CACHE,
            ExistingPeriodicWorkPolicy.REPLACE,
            worker
        )
    }

    companion object {

        private const val TAG_CLEAR_CACHE = "update_apps"
        private const val REPEAT_INTERVAL_DAYS = 1L
    }
}