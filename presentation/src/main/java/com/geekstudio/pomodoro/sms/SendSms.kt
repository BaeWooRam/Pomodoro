package com.geekstudio.pomodoro.sms

import java.lang.Exception

interface SendSms {
    interface Target{
        fun target(phoneNumbers:List<String>):Message
        fun target(phoneNumber:String):Message
    }

    interface Message{
        fun message(msg:String):SendSms
    }

    fun listener(listener: SmsListener):SendSms
    fun build():SendSmsBuilder.Sender?

    interface SmsListener{
        fun onSuccess()
        fun onFailure(e: Exception)
    }
}