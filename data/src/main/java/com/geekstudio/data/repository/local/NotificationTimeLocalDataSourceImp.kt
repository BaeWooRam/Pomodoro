package com.geekstudio.data.repository.local

import com.geekstudio.data.AppPreferences
import com.geekstudio.entity.NotificationTime

class NotificationTimeLocalDataSourceImp(private val appPreferences: AppPreferences) : NotificationTimeLocalDataSource {
    override fun setNotificationWorkTime(notificationTime: NotificationTime) {
        appPreferences.setNotificationWorkTime(notificationTime.toString())
    }

    override fun setNotificationRestTime(notificationTime: NotificationTime) {
        appPreferences.setNotificationRestTime(notificationTime.toString())
    }

    /**
     * 알림 시간
     * 기본 시간 25분
     */
    override fun getNotificationWorkTime(): NotificationTime {
        val time = appPreferences.getNotificationWorkTime()

        return if(time != null)
            NotificationTime.covertNotification(time)
        else
            NotificationTime(0,25,0)
    }

    override fun getNotificationRestTime(): NotificationTime {
        val restTime = appPreferences.getNotificationRestTime()

        return if(restTime != null)
            NotificationTime.covertNotification(restTime)
        else
            NotificationTime(0,5,0)
    }

}