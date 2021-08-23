package com.geekstudio.data.send

import android.telephony.SmsManager
import java.lang.Exception

class SendSmsBuilder : BaseSend<String, SendSmsBuilder.Sender> {
    private var message: String? = null
    private val phoneNumbers: ArrayList<String> = arrayListOf()
    private var listener: BaseSend.SmsListener? = null

    override fun target(phoneNumbers: List<String>): BaseSend<String, Sender> {
        this@SendSmsBuilder.phoneNumbers.addAll(phoneNumbers)
        return this@SendSmsBuilder
    }

    override fun target(phoneNumber: String): BaseSend<String, Sender> {
        this@SendSmsBuilder.phoneNumbers.add(phoneNumber)
        return this@SendSmsBuilder
    }

    override fun message(msg: String): BaseSend<String, Sender> {
        this@SendSmsBuilder.message = msg
        return this@SendSmsBuilder
    }

    override fun listener(listener: BaseSend.SmsListener): BaseSend<String, Sender> {
        this@SendSmsBuilder.listener = listener
        return this@SendSmsBuilder
    }

    override fun build(): Sender? {
        if (message.isNullOrEmpty())
            return null

        if (phoneNumbers.isEmpty())
            return null

        return Sender(phoneNumbers, message!!, listener)
    }

    /**
     * Sms 보내는 요청자
     */
    class Sender(
        private val phoneNumbers: List<String>,
        private val msg: String,
        private val listener: BaseSend.SmsListener?,
    ) {
        private val smsManager = SmsManager.getDefault()

        suspend fun send() {
            kotlin.runCatching {
                phoneNumbers.forEach { phoneNumber ->
                    smsManager.sendTextMessage(phoneNumber, null, msg, null, null)
                }
            }.onSuccess {
                listener?.onSuccess()
            }.onFailure {
                listener?.onFailure(Exception(it))
            }
        }
    }
}