package com.geekstudio.data.repository.remote.friend

import com.geekstudio.data.listener.TaskListener
import com.geekstudio.entity.NotificationTime
import com.kakao.sdk.talk.model.Friend

interface KakaoFriendRemoteDataSource {
    fun getFriendList(taskListener: TaskListener<List<Friend>>)
}