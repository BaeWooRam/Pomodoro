package com.geekstudio.pomodoro.monitor

import android.content.Context
import android.util.Log
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.data.notification.RestNotification
import com.geekstudio.data.notification.WorkNotification
import com.geekstudio.data.repository.local.NotificationTimeLocalDataSourceImp
import com.geekstudio.data.repository.local.RecipientLocalDataSourceImp
import com.geekstudio.data.send.BaseSend
import com.geekstudio.data.send.SendSmsBuilder
import com.geekstudio.entity.NotificationState
import com.geekstudio.pomodoro.Config
import com.geekstudio.pomodoro.R
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
    private val context: Context,
    private val notificationTimeLocalDataSourceImp: NotificationTimeLocalDataSourceImp,
    private val recipientLocalDataSourceImp: RecipientLocalDataSourceImp,
    private val restNotification: RestNotification,
    private val workNotification: WorkNotification,
) : TimerTask(), BaseSend.SmsListener {
    private val debugTag: String = javaClass.simpleName
    private val scope = CoroutineScope(Dispatchers.Default)
    private var state: NotificationState = NotificationState.Work
    private var time: Int = 0

    private var workMessageSender:SendSmsBuilder.Sender? = null
    private var restMessageSender:SendSmsBuilder.Sender? = null

    override fun run() {
        Log.d(javaClass.simpleName, "NotificationMonitorTimerTask start time = $time")
        time += Config.CONNECTION_MONITOR_TIME

        initWorkMessageSender()
        initRestMessageSender()

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
    private fun initWorkMessageSender(){
        scope.launch {
            val target = recipientLocalDataSourceImp.getAllRecipient(RecipientEntity.Type.Sms)
            Log.d(javaClass.simpleName, "initWorkMessageSender() target = $target")

            workMessageSender = SendSmsBuilder()
                .target(convertPhoneNumberList(target))
                .message(notificationTimeLocalDataSourceImp.getNotificationWorkContent() ?: context.getString(R.string.notification_time_content))
                .listener(this@NotificationMonitorTimerTask)
                .build()
        }
    }

    /**
     * 휴식 SMS 전송자
     */
    private fun initRestMessageSender() {
        scope.launch {
            val target = recipientLocalDataSourceImp.getAllRecipient()
            Log.d(javaClass.simpleName, "initRestMessageSender() target = $target")

            restMessageSender =  SendSmsBuilder()
                .target(convertPhoneNumberList(target))
                .message(notificationTimeLocalDataSourceImp.getNotificationRestContent() ?: context.getString(R.string.notification_rest_time_content))
                .listener(this@NotificationMonitorTimerTask)
                .build()
        }
    }

    private fun convertPhoneNumberList(data:List<RecipientEntity>):List<String>{
        val temp = mutableListOf<String>()

        data.forEach { data ->
            temp.add(data.sendId)
        }

        return temp
    }

    override fun onSuccess() {
        Log.d(javaClass.simpleName, "SendSms is onSuccess!")
    }

    override fun onFailure(e: Exception) {
        Log.d(javaClass.simpleName, "SendSms is Failure : ${e.message}")
    }
}