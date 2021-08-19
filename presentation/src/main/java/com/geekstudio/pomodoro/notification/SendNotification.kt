package com.geekstudio.pomodoro.notification

import android.content.ClipDescription
import android.content.Intent

interface SendNotification {
    interface Target{
        fun target(target: NotificationTarget):Data
    }

    interface Data {
        fun data(data: NotificationData):SendNotification
    }

    fun build():SendNotificationBuilder.Builder?

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
        val notificationId: Int,
        val notificationIntent: Intent
    )
}