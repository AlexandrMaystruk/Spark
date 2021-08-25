package com.gmail.maystruks08.spark.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gmail.maystruks08.spark.MainActivity
import com.gmail.maystruks08.spark.R
import javax.inject.Inject

class NotificationController @Inject constructor(private val context: Context) {

    init {
        createNotificationChannel()
    }

    fun showNotification(
        notificationId: Int,
        title: String,
        contentPreviewText: String,
        contentText: String,
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        with(NotificationManagerCompat.from(context)) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(contentPreviewText)
                .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val name = context.getString(R.string.action_read)
        val descriptionText = context.getString(R.string.action_read)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "Spark messages"
    }

}