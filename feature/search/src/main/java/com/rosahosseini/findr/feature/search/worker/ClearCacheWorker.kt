package com.rosahosseini.findr.feature.search.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rosahosseini.findr.domain.search.SearchRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
internal class ClearCacheWorker @AssistedInject constructor(
    private val searchRepository: SearchRepository,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            searchRepository.clearExpiredData(EXPIRATION_TIME_MILLIS)
            Result.success()
        } catch (ignored: Exception) {
            Result.failure()
        }
    }

    companion object {
        private const val EXPIRATION_TIME_MILLIS = 7 * 24 * 60 * 60 * 1000L
    }
}
