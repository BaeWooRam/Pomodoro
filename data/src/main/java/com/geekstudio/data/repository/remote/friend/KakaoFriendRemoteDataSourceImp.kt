package com.geekstudio.data.repository.remote.friend

import android.content.Context
import android.util.Log
import com.geekstudio.data.AppPreferences
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.entity.NotificationTime
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import java.lang.Exception

class KakaoFriendRemoteDataSourceImp(
    private val talkApiClient: TalkApiClient
) : KakaoFriendRemoteDataSource {

    override fun getFriendList(taskListener: TaskListener<List<Friend>>) {
        talkApiClient.friends { friends, error ->
            if (error != null) {
                taskListener.onFailure(Exception(error))
            } else {
                if (friends == null || friends.elements.isNullOrEmpty()) {
                    taskListener.onSuccess(arrayListOf())
                } else {
                    taskListener.onSuccess(friends!!.elements!!)
                }
            }
        }
    }
}