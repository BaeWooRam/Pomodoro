package com.geekstudio.pomodoro.ui.test

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.data.oauth.KakaoLogin
import com.geekstudio.data.oauth.OAuth
import com.geekstudio.data.oauth.OAuthUser
import com.geekstudio.data.repository.remote.friend.KakaoFriendRemoteDataSourceImp
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.databinding.ActivityMainBinding
import com.geekstudio.pomodoro.ui.base.BaseActivity
import com.geekstudio.pomodoro.ui.main.MainActivity
import com.geekstudio.pomodoro.util.ActivityUtil
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import org.koin.android.ext.android.inject
import java.lang.Exception

class KakaoTestActivity : BaseActivity(R.layout.activity_splash) {
    private val kakaoFriendRemoteDataSourceImp by inject<KakaoFriendRemoteDataSourceImp>()
    private val kakaoLogin by inject<KakaoLogin>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //카카오_로그인_테스트
        kakaoLogin?.login(object : OAuth.OAuthListener<OAuthUser>{
            override fun success(data: OAuthUser) {
                Log.d(javaClass.simpleName,"onSuccess() result = $data")

                //카카오_친구_목록_테스트
                kakaoFriendRemoteDataSourceImp.getFriendList(object :TaskListener<List<Friend>>{
                    override fun onSuccess(result: List<Friend>) {
                        Log.d(javaClass.simpleName, "onSuccess() result = $result")
                    }

                    override fun onFailure(e: Exception) {
                        Log.d(javaClass.simpleName, "onFailure() e = ${e.message}")
                        showMessage(e.message ?: "에러 발생")
                    }
                })
            }

            override fun failure(e: Exception) {
                Log.d(javaClass.simpleName, "onFailure() e = ${e.message ?: "에러 발생"}")
                showMessage(e.message ?: "에러 발생")
            }
        })
    }
}