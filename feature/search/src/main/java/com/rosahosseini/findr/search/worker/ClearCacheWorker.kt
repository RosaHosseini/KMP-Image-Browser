package com.rosahosseini.findr.search.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rosahosseini.findr.repository.SearchRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ClearCacheWorker @AssistedInject constructor(
    private val searchRepository: SearchRepository,
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            searchRepository.clearExpiredData(EXPIRE_TIME_MILLIS)
            Result.success()
        } catch (ignored: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val EXPIRE_TIME_MILLIS = 1000 * 60 * 60 * 24 * 7L
    }
}