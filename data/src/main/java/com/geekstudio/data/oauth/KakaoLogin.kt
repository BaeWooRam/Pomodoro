package com.geekstudio.data.oauth

import android.content.Context
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient


class KakaoLogin(private val context: Context) : OAuth {
    override fun login(listener: OAuth.OAuthListener<OAuthUser>) {
        //유효성 검사
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    executeLogin(listener)
                }else{
                    getOAuthUser(listener)
                }
            }
        } else {
            executeLogin(listener)
        }
    }

    private fun executeLogin(listener: OAuth.OAuthListener<OAuthUser>) {
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            when {
                error != null -> listener?.failure(Exception(error))
                token == null -> listener?.failure(Exception("Token is null"))
                token != null -> getOAuthUser(listener)
            }
        }
    }

    private fun getOAuthUser(listener: OAuth.OAuthListener<OAuthUser>) {
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                listener.failure(Exception(error))
                return@me
            }

            if (user != null) {
                listener.success(
                    OAuthUser(
                        user.id.toString(),
                        user.kakaoAccount?.profile?.nickname ?: "null",
                        user.kakaoAccount?.email ?: "null"
                    )
                )
            }
        }
    }

    override fun logout(listener: OAuth.OAuthListener<Boolean>) {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                listener?.failure(Exception(error))
            } else {
                listener?.success(true)
            }
        }
    }

    override fun disconnect(listener: OAuth.OAuthListener<Boolean>) {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                listener?.failure(Exception(error))
            } else {
                listener?.success(true)
            }
        }
    }

}