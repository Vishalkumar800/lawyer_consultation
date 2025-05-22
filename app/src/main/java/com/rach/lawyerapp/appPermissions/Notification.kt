package com.rach.lawyerapp.appPermissions

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.rach.lawyerapp.R
import com.rach.lawyerapp.utils.CHANNEL_ID
import com.rach.lawyerapp.utils.NOTIFICATION_ID
import com.rach.lawyerapp.utils.NOTIFICATION_TITLE
import com.rach.lawyerapp.utils.VISHAL_CHANNEL_NAME
import com.rach.lawyerapp.utils.VISHAL_NOTIFICATION_CHANNEL_DESCRIPTION

fun makeNotification(message: String, context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            VISHAL_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = VISHAL_NOTIFICATION_CHANNEL_DESCRIPTION
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.love)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))

    val notificationManager = NotificationManagerCompat.from(context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(NOTIFICATION_ID, builder.build())

        } else {
            if (context is Activity) {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    } else {
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

}