package com.geekstudio.data.repository.local

import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.entity.NotificationTime


interface NotificationTimeLocalDataSource {
    fun setNotificationTime(notificationTime: NotificationTime)
    fun setNotificationRestTime(notificationTime: NotificationTime)
    fun getNotificationTime(): NotificationTime
    fun getNotificationRestTime(): NotificationTime
}