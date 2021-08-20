package com.geekstudio.data.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


abstract class BaseNotification(protected val context: Context){
    private var notificationBuilderTarget: NotificationTarget = getNotificationTarget()
    private var notificationBuilderData: NotificationData = getNotificationData()
    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    abstract fun getNotificationTarget():NotificationTarget
    abstract fun getNotificationData():NotificationData

    init {
        Log.d(javaClass.simpleName, "init()")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !hasNotificationChannel())
            createChannel()
    }

    /**
     * 노티피케이션 채널 생성
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        Log.d(javaClass.simpleName, "createChannel()")
        var channel = NotificationChannel(
            notificationBuilderTarget.channelId,
            notificationBuilderTarget.channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = notificationBuilderTarget.channelDescription
        channel.enableLights(true)
        channel.lightColor = Color.GREEN

        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun hasNotificationChannel():Boolean = notificationManager.getNotificationChannel(notificationBuilderTarget.channelId) != null

    /**
     * 노티피케이션 전송
     */
    fun send() {
        Log.d(javaClass.simpleName, "send()")
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationBuilderData.notificationIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            NotificationCompat.Builder(context, notificationBuilderTarget.channelId)
        else
            NotificationCompat.Builder(context).setPriority(Notification.PRIORITY_DEFAULT)

        builder.setSmallIcon(notificationBuilderData.smallImage)
            .setContentTitle(notificationBuilderData.title)
            .setContentText(notificationBuilderData.content)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))

        notificationManager.notify(notificationBuilderData.notificationId, builder.build())
    }

    /**
     *
     */
    data class NotificationTarget(
        val channelId: String,
        val channelName: String,
        val channelDescription: String
    )

    /**
     *
     */
    data class NotificationData(
        val title: String,
        val content: String,
        @DrawableRes val smallImage: Int,
        val notificationId: Int,
        val notificationIntent: Intent
    )
}