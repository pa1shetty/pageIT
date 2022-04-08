package com.example.pageit.helper.workManager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pageit.R
import com.example.pageit.data.database.DatabaseRepository
import com.example.pageit.data.module.beResponse.BeResponse
import com.example.pageit.helper.notification.PushNotification.showNotification
import com.example.pageit.network.NetworkRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@HiltWorker
class SavePageDetailsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val databaseRepository: DatabaseRepository,
    private val networkRepository: NetworkRepository
) :
    CoroutineWorker(context, workerParameters) {
    companion object {
        const val CHANNEL_ID = "page_data_save"
        const val NOTIFICATION_ID = 1
    }

    override suspend fun doWork(): Result {
        return try {
            fetchData()
            Result.success()
        } catch (throwable: Throwable) {
            showNotification(
                applicationContext.getString(R.string.something_went_wrong_notification),
                applicationContext
            )
            Result.failure()
        }
    }

    private fun fetchData() {
        val pages = databaseRepository.getFirstPage()
        if (pages.isNotEmpty()) {
            val page = pages[0]
            networkRepository.savePageDetails(page)
                .enqueue(object : Callback<BeResponse> {
                    override fun onResponse(
                        call: Call<BeResponse>,
                        response: Response<BeResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                if (it.status == "success") {
                                    showNotification(
                                        applicationContext.getString(R.string.data_uploaded_notification),
                                        applicationContext
                                    )
                                    return
                                }
                            }
                            showNotification(
                                applicationContext.getString(R.string.something_went_wrong),
                                applicationContext
                            )
                        }
                    }

                    override fun onFailure(call: Call<BeResponse>, t: Throwable) {
                        showNotification(
                            applicationContext.getString(R.string.something_went_wrong),
                            applicationContext
                        )
                    }
                })
        }
    }


}