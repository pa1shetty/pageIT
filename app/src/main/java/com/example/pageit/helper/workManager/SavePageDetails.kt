package com.example.pageit.helper.workManager

import android.content.Context
import androidx.work.*

import java.util.concurrent.TimeUnit

object SavePageDetails {
    private const val INTERVAL_TIME = 15L
    private const val WORKER_NAME = "save_page_details"

    fun savePageDetailsWorker(context: Context) {

        val savePageDetails = PeriodicWorkRequest.Builder(
            SavePageDetailsWorker::class.java,
            INTERVAL_TIME,
            TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WORKER_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                savePageDetails
            )
    }

}