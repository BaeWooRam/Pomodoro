package com.geekstudio.data.repository.local

import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.entity.NotificationTime


interface NotificationTimeLocalDataSource {
    fun setNotificationWorkTime(notificationTime: NotificationTime)
    fun setNotificationRestTime(notificationTime: NotificationTime)
    fun getNotificationWorkTime(): NotificationTime
    fun getNotificationRestTime(): NotificationTime

    fun setNotificationWorkContent(content: String)
    fun setNotificationRestContent(content: String)
    fun getNotificationWorkContent(): String?
    fun getNotificationRestContent(): String?
}