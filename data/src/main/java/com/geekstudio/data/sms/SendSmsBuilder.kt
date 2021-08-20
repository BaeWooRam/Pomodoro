package com.geekstudio.data.sms

import android.telephony.SmsManager
import java.lang.Exception

class SendSmsBuilder : SendSms, SendSms.Target, SendSms.Message {
    private var message: String? = null
    private val phoneNumbers: ArrayList<String> = arrayListOf()
    private var listener: SendSms.SmsListener? = null

    override fun target(phoneNumbers: List<String>): SendSms.Message {
        this@SendSmsBuilder.phoneNumbers.addAll(phoneNumbers)
        return this@SendSmsBuilder
    }

    override fun target(phoneNumber: String): SendSms.Message {
        this@SendSmsBuilder.phoneNumbers.add(phoneNumber)
        return this@SendSmsBuilder
    }

    override fun message(msg: String): SendSms {
        this@SendSmsBuilder.message = msg
        return this@SendSmsBuilder
    }

    override fun listener(listener: SendSms.SmsListener): SendSms {
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
        private val listener: SendSms.SmsListener?,
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