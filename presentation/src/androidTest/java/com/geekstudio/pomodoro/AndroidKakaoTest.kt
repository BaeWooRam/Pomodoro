package com.geekstudio.pomodoro

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.data.oauth.KakaoLogin
import com.geekstudio.data.oauth.OAuth
import com.geekstudio.data.oauth.OAuthUser
import com.geekstudio.data.repository.remote.friend.KakaoFriendRemoteDataSourceImp
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.talk.model.Friend
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AndroidKakaoTest {
    private var kakaoFriendRemoteDataSourceImp:KakaoFriendRemoteDataSourceImp? = null
    private var kakaoLogin: KakaoLogin? = null
    private var appContext:Context? = null

    @Before
    fun init(){
        // Kakao SDK 초기화
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        KakaoSdk.init(appContext!!, "bf528b934133e275673174bccc1a8785")

        kakaoFriendRemoteDataSourceImp = KakaoFriendRemoteDataSourceImp(TalkApiClient.instance)
        kakaoLogin = KakaoLogin(appContext!!)
    }

    @Test
    fun `카카오_해쉬키_테스트`(){
        Log.d("카카오_해쉬키_테스트", Utility.getKeyHash(appContext!!))
    }

    @Test
    fun `카카오_로그인_테스트`(){
        kakaoLogin?.login(object : OAuth.OAuthListener<OAuthUser>{
            override fun success(data: OAuthUser) {
                Log.d("카카오_로그인_테스트","onSuccess() result = $data")
            }

            override fun failure(e: Exception) {
                Log.d("카카오_로그인_테스트", "onFailure() e = ${e.message ?: "에러 발생"}")
            }
        })
    }

    @Test
    fun `카카오_친구_목록_테스트`(){
        kakaoFriendRemoteDataSourceImp?.getFriendList(object : TaskListener<List<Friend>> {
            override fun onSuccess(result: List<Friend>) {
                Log.d("카카오_친구_목록_테스트","onSuccess() result = $result")
            }

            override fun onFailure(e: Exception) {
                Log.d("카카오_친구_목록_테스트", "onFailure() e = ${e.message ?: "에러 발생"}")
            }
        })
    }
}