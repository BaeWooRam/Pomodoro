package com.geekstudio.pomodoro.monitor

import android.util.Log
import com.geekstudio.data.notification.RestNotification
import com.geekstudio.data.notification.WorkNotification
import com.geekstudio.data.repository.local.NotificationTimeLocalDataSourceImp
import com.geekstudio.data.send.BaseSend
import com.geekstudio.data.send.SendSmsBuilder
import com.geekstudio.entity.NotificationState
import com.geekstudio.pomodoro.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

/**
 * SensorData DB 저장하기
 * @author 배우람
 */
class NotificationMonitorTimerTask(
    private val notificationTimeLocalDataSourceImp: NotificationTimeLocalDataSourceImp,
    private val restNotification: RestNotification,
    private val workNotification: WorkNotification,
) : TimerTask(), BaseSend.SmsListener {
    private val debugTag: String = javaClass.simpleName
    private val scope = CoroutineScope(Dispatchers.Default)
    private var state: NotificationState = NotificationState.Work
    private var time: Int = 0

    override fun run() {
        Log.d(javaClass.simpleName, "NotificationMonitorTimerTask start time = $time")
        time += Config.CONNECTION_MONITOR_TIME

        val workMessageSender = getWorkMessageSender()
        val restMessageSender = getRestMessageSender()

        when (state) {
            NotificationState.Work -> {
                if (notificationTimeLocalDataSourceImp.getNotificationWorkTime().toSecond() != time)
                    return

                scope.launch {
                    Log.d(javaClass.simpleName, "WorkNotification send()")
                    workNotification.send()
                    workMessageSender?.send()
                    state = NotificationState.Rest
                    time = 0
                }
            }
            NotificationState.Rest -> {
                if (notificationTimeLocalDataSourceImp.getNotificationRestTime().toSecond() != time)
                    return

                scope.launch {
                    Log.d(javaClass.simpleName, "RestNotification send()")
                    restNotification.send()
                    restMessageSender?.send()
                    state = NotificationState.Work
                    time = 0
                }
            }
        }
    }


    /**
     * 일 SMS 전송자
     */
    private fun getWorkMessageSender(): SendSmsBuilder.Sender? {
        return SendSmsBuilder()
            .target("01092055472")
            .message("일하세요")
            .listener(this@NotificationMonitorTimerTask)
            .build()
    }


    /**
     * 휴식 SMS 전송자
     */
    private fun getRestMessageSender(): SendSmsBuilder.Sender? {
        return SendSmsBuilder()
            .target("01092055472")
            .message("휴식하세요")
            .listener(this@NotificationMonitorTimerTask)
            .build()
    }

    override fun onSuccess() {
        Log.d(javaClass.simpleName, "SendSms is onSuccess!")
    }

    override fun onFailure(e: Exception) {
        Log.d(javaClass.simpleName, "SendSms is Failure : ${e.message}")
    }
}