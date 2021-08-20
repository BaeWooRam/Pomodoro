package com.geekstudio.data.repository.local

import com.geekstudio.data.AppPreferences
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.entity.NotificationTime

class NotificationTimeLocalDataSourceImp(private val appPreferences: AppPreferences) : NotificationTimeLocalDataSource {
    override fun setNotificationTime(notificationTime: NotificationTime) {
        appPreferences.setNotificationTime(notificationTime.toString())
    }

    override fun setNotificationRestTime(notificationTime: NotificationTime) {
        appPreferences.setNotificationRestTime(notificationTime.toString())
    }

    override fun getNotificationTime(): NotificationTime {
        val time = appPreferences.getNotificationTime()

        return if(time != null)
            NotificationTime.covertNotification(time)
        else
            NotificationTime(0,0,0)
    }

    override fun getNotificationRestTime(): NotificationTime {
        val restTime = appPreferences.getNotificationRestTime()

        return if(restTime != null)
            NotificationTime.covertNotification(restTime)
        else
            NotificationTime(0,0,0)
    }

}