package com.geekstudio.data.send

interface BaseSend<Target, Sender> {
    fun target(friends: List<Target>): BaseSend<Target, Sender>
    fun target(friend: Target): BaseSend<Target, Sender>
    fun message(msg: String): BaseSend<Target, Sender>
    fun listener(listener: SmsListener): BaseSend<Target, Sender>
    fun build(): Sender?

    interface SmsListener {
        fun onSuccess()
        fun onFailure(e: Exception)
    }
}