package com.fernando.core.infrastructure.managers.notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.fernando.core.R
import com.fernando.core.domain.entities.NotificationEntity
import com.fernando.core.domain.managers.NotificationsManager
import com.fernando.core.infrastructure.handlers.ImageLoader
import com.fernando.ui_ktx.content.addSingleTopFlags
import com.fernando.ui_ktx.kotlin.isNotNull
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Named


class NotificationsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val imageLoader: ImageLoader,
    private val activity: Class<out Activity>
) : NotificationsManager {
    companion object {
        private const val NOTIFICATION_ID = 1
    }

    private val channelId = context.getString(R.string.channel_id)
    private val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun display(notification: NotificationEntity) {
        val notificationBuilder = buildNotification(notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = buildChannel()
            manager.createNotificationChannel(channel)
        }

        manager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private suspend fun buildNotification(notification: NotificationEntity): NotificationCompat.Builder {
        val image = notification.imageUrl?.let { imageLoader.load(it) }
        val color = ContextCompat.getColor(context, android.R.color.black)
        val pendingIntent = getPendingIntent(notification.link)

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_notification)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setLargeIcon(image)
            .setAutoCancel(true)
            .setColor(color)
            .setContentIntent(pendingIntent)
    }

    private fun getPendingIntent(link: String?): PendingIntent {
        val notifyIntent = if (link.isNotNull()) {
            Intent(Intent.ACTION_VIEW).apply {
                addSingleTopFlags()
                data = Uri.parse(link)
            }
        } else {
            Intent(context, activity).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
        }

        return PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_ONE_SHOT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildChannel(): NotificationChannel = with(context) {
        val name = getString(R.string.channel_name)
        val description = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH

        return NotificationChannel(channelId, name, importance).apply {
            setDescription(description)
            enableLights(true)
            enableVibration(true)
        }
    }
}