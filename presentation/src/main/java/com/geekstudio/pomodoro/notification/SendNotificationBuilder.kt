package com.geekstudio.pomodoro.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.geekstudio.pomodoro.R

class SendNotificationBuilder(private val context: Context) : SendNotification,
    SendNotification.Target, SendNotification.Data {
    private var notificationTarget: SendNotification.NotificationTarget? = null
    private var notificationData: SendNotification.NotificationData? = null

    override fun target(target: SendNotification.NotificationTarget): SendNotification.Data {
        this@SendNotificationBuilder.notificationTarget = target
        return this@SendNotificationBuilder
    }

    override fun data(data: SendNotification.NotificationData): SendNotification {
        this@SendNotificationBuilder.notificationData = data
        return this@SendNotificationBuilder
    }

    override fun build(): Builder? {
        if (notificationTarget == null)
            return null

        if (notificationData == null)
            return null

        return Builder(context, notificationTarget!!, notificationData!!)
    }

    /**
     *
     */
    class Builder(
        private val context: Context,
        private val notificationTarget: SendNotification.NotificationTarget,
        private val notificationData: SendNotification.NotificationData
    ) {
        private val notificationManager:NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        init {
            //채널 초기화
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !hasNotificationChannel())
                createChannel()
        }

        /**
         * 노티피케이션 채널 생성
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun createChannel() {
            var channel = NotificationChannel(
                notificationTarget.channelId,
                notificationTarget.channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = notificationTarget.channelDescription
            channel.enableLights(true)
            channel.lightColor = Color.GREEN

            notificationManager.createNotificationChannel(channel)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun hasNotificationChannel():Boolean = notificationManager.getNotificationChannel(notificationTarget.channelId) != null

        /**
         * 노티피케이션 전송
         */
        fun send() {
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationData.notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                NotificationCompat.Builder(context, notificationTarget.channelId)
            else
                NotificationCompat.Builder(context).setPriority(Notification.PRIORITY_DEFAULT)

            builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(notificationData.title)
                .setContentText(notificationData.content)
                .setContentIntent(pendingIntent)

            notificationManager.notify(notificationData.notificationId, builder.build())
        }
    }
}