package com.geekstudio.pomodoro.ui.main

import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.data.repository.local.NotificationTimeLocalDataSourceImp
import com.geekstudio.entity.NotificationTime
import com.geekstudio.pomodoro.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(
    private val notificationTimeLocalDataSourceImp: NotificationTimeLocalDataSourceImp
) : BaseViewModel() {

    fun setNotificationRestTime(notificationTime: NotificationTime) {
        notificationTimeLocalDataSourceImp.setNotificationTime(notificationTime)
    }

    fun setNotificationTime(notificationTime: NotificationTime) {
        notificationTimeLocalDataSourceImp.setNotificationRestTime(notificationTime)
    }

    fun getNotificationRestTime(): NotificationTime = notificationTimeLocalDataSourceImp.getNotificationRestTime()

    fun getNotificationTime(): NotificationTime = notificationTimeLocalDataSourceImp.getNotificationTime()

}