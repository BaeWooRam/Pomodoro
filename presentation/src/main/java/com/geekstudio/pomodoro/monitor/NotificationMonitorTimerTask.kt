package com.geekstudio.pomodoro.monitor

import android.util.Log
import com.geekstudio.data.notification.BaseNotification
import com.geekstudio.data.notification.RestNotification
import com.geekstudio.data.notification.WorkNotification
import com.geekstudio.data.repository.local.NotificationTimeLocalDataSourceImp
import com.geekstudio.entity.NotificationState
import com.geekstudio.entity.TimeUnit
import com.geekstudio.pomodoro.Config
import java.util.*

/**
 * SensorData DB 저장하기
 * @author 배우람
 */
class NotificationMonitorTimerTask(
    private val notificationTimeLocalDataSourceImp: NotificationTimeLocalDataSourceImp,
    private val restNotification: RestNotification,
    private val workNotification: WorkNotification,
) : TimerTask() {
    private val debugTag: String = javaClass.simpleName
    private var state: NotificationState = NotificationState.Work
    private var time: Int = 0

    override fun run() {
        Log.d(javaClass.simpleName, "NotificationMonitorTimerTask start time = $time")
        time += Config.CONNECTION_MONITOR_TIME

        when (state) {
            NotificationState.Work -> {
                if (notificationTimeLocalDataSourceImp.getNotificationRestTime().toSecond() == time){
                    Log.d(javaClass.simpleName, "WorkNotification send()")
                    workNotification.send()
                    state = NotificationState.Rest
                    time = 0
                }
            }
            NotificationState.Rest -> {
                if (notificationTimeLocalDataSourceImp.getNotificationRestTime().toSecond() == time){
                    Log.d(javaClass.simpleName, "RestNotification send()")
                    restNotification.send()
                    state = NotificationState.Work
                    time = 0
                }
            }
        }
    }
}