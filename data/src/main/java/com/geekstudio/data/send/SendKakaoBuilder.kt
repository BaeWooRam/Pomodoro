package com.geekstudio.data.send

import android.telephony.SmsManager
import com.kakao.sdk.talk.TalkApiClient

import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.talk.model.MessageFailureInfo
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.TextTemplate
import java.lang.Exception

class SendKakaoBuilder : BaseSend<Friend, SendKakaoBuilder.Sender> {
    private var message: String? = null
    private val friends: ArrayList<Friend> = arrayListOf()
    private var listener: BaseSend.SmsListener? = null

    override fun target(friends: List<Friend>): BaseSend<Friend, Sender> {
        this@SendKakaoBuilder.friends.addAll(friends)
        return this@SendKakaoBuilder
    }

    override fun target(friend: Friend): BaseSend<Friend, Sender> {
        this@SendKakaoBuilder.friends.add(friend)
        return this@SendKakaoBuilder
    }

    override fun message(msg: String): BaseSend<Friend, Sender> {
        this@SendKakaoBuilder.message = msg
        return this@SendKakaoBuilder
    }

    override fun listener(listener: BaseSend.SmsListener): BaseSend<Friend, Sender> {
        this@SendKakaoBuilder.listener = listener
        return this@SendKakaoBuilder
    }

    override fun build(): Sender? {
        if (message.isNullOrEmpty())
            return null

        if (friends.isEmpty())
            return null

        return Sender(friends, message!!, listener)
    }

    /**
     * Sms 보내는 요청자
     */
    class Sender(
        private val friends: List<Friend>,
        private val msg: String,
        private val listener: BaseSend.SmsListener?,
    ) {

        suspend fun send() {
            kotlin.runCatching {
                TalkApiClient.instance.sendDefaultMessage(
                    getUuidList(),
                    getMessageTemplate()
                ) { result, error ->
                    if (error != null) {
                        listener?.onFailure(Exception(error))
                        return@sendDefaultMessage
                    }

                    if (result != null) {
                        if (result.failureInfos != null) {
                            listener?.onFailure(KakaoSendException(result.failureInfos))
                        } else
                            listener?.onSuccess()
                    }
                }
            }.onSuccess {
                listener?.onSuccess()
            }.onFailure {
                listener?.onFailure(Exception(it))
            }
        }

        private fun getMessageTemplate(): TextTemplate {
            return TextTemplate("테스트", Link(), null, null)
        }

        private fun getUuidList(): List<String> {
            val temp = arrayListOf<String>()

            friends.forEach { friend ->
                temp.add(friend.uuid)
            }

            return temp
        }

        class KakaoSendException(val failureInfoList: List<MessageFailureInfo>?) : Exception()
    }
}