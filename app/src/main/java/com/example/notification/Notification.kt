package com.example.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notification = NotificationCompat.Builder(context!!, channelID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(intent!!.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("This is big text")
            )
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.notify(notificationID, notification)

    }

}