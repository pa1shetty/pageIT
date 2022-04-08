package com.example.pageit.helper.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pageit.R
import com.example.pageit.helper.workManager.SavePageDetailsWorker
import com.example.pageit.ui.MainActivity

object PushNotification {
    private const val channelName = "Page Data Upload"
    private const val channelDescription = "Notification when page data uploaded to backend"

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(message: String, applicationContext: Context) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                applicationContext,
                0, intent, PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                applicationContext,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }


        val notification = NotificationCompat.Builder(
            applicationContext,
            SavePageDetailsWorker.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                SavePageDetailsWorker.CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = channelDescription
            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(SavePageDetailsWorker.NOTIFICATION_ID, notification.build())
        }
    }

}