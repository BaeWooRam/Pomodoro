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
import com.geekstudio.entity.UserState
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
    private var state: UserState = UserState.Work
    private var time: Int = 0

    private var workMessageSender:SendSmsBuilder.Sender? = null
    private var restMessageSender:SendSmsBuilder.Sender? = null

    init {
        initWorkMessageSender()
        initRestMessageSender()
    }

    override fun run() {
        Log.d(debugTag, "NotificationMonitorTimerTask start! time = $time, state = $state")
        time += Config.CONNECTION_MONITOR_TIME

        when (state) {
            UserState.Work -> {
                if (!isSendTime(notificationTimeLocalDataSourceImp.getNotificationWorkTime().toSecond(), time))
                    return

                scope.launch {
                    Log.d(debugTag, "WorkNotification send()")
                    restNotification.send()

                    val target = recipientLocalDataSourceImp.getAllRecipient(RecipientEntity.Type.Sms)
                    Log.d(debugTag, "WorkNotification Sms send() target = $target")
                    restMessageSender?.send(convertPhoneNumberList(target))

                    state = UserState.Rest
                    time = 0
                }
            }
            UserState.Rest -> {
                if (!isSendTime(notificationTimeLocalDataSourceImp.getNotificationRestTime().toSecond(), time))
                    return

                scope.launch {
                    Log.d(debugTag, "RestNotification send()")
                    workNotification.send()

                    val target = recipientLocalDataSourceImp.getAllRecipient(RecipientEntity.Type.Sms)
                    Log.d(debugTag, "RestNotification Sms send() target = $target")
                    workMessageSender?.send(convertPhoneNumberList(target))

                    state = UserState.Work
                    time = 0
                }
            }
        }
    }

    private fun isSendTime(standardTime: Int, targetTime: Int): Boolean {
        if (standardTime == 0 || targetTime == 0)
            return false

        if(standardTime < targetTime)
            return true


        return targetTime == standardTime
    }


    /**
     * 일 SMS 전송자
     */
    private fun initWorkMessageSender(){
        if(workMessageSender != null)
            return

        workMessageSender = SendSmsBuilder()
            .target(arrayListOf())
            .message(notificationTimeLocalDataSourceImp.getNotificationWorkContent() ?: context.getString(R.string.notification_time_content))
            .listener(this@NotificationMonitorTimerTask)
            .build()
    }

    /**
     * 휴식 SMS 전송자
     */
    private fun initRestMessageSender() {
        scope.launch {
            val target = recipientLocalDataSourceImp.getAllRecipient()
            Log.d(debugTag, "initRestMessageSender() target = $target")

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
        Log.d(debugTag, "SendSms is onSuccess!")
    }

    override fun onFailure(e: Exception) {
        Log.d(debugTag, "SendSms is Failure : ${e.message}")
    }
}