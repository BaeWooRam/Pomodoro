package com.geekstudio.pomodoro.ui.main

import com.geekstudio.data.repository.local.NotificationTimeLocalDataSourceImp
import com.geekstudio.entity.NotificationTime
import com.geekstudio.pomodoro.ui.base.BaseViewModel

class MainViewModel(
    private val notificationTimeLocalDataSourceImp: NotificationTimeLocalDataSourceImp
) : BaseViewModel() {

    fun setNotificationRestTime(notificationTime: NotificationTime) {
        notificationTimeLocalDataSourceImp.setNotificationWorkTime(notificationTime)
    }

    fun setNotificationTime(notificationTime: NotificationTime) {
        notificationTimeLocalDataSourceImp.setNotificationRestTime(notificationTime)
    }

    fun getNotificationRestTime(): NotificationTime = notificationTimeLocalDataSourceImp.getNotificationRestTime()

    fun getNotificationTime(): NotificationTime = notificationTimeLocalDataSourceImp.getNotificationWorkTime()

}