package com.geekstudio.data.oauth

interface OAuth {
    enum class Type(val value: Int) {
        NONE(0), KAKAO(1)
    }

    fun login(listener: OAuthListener<OAuthUser>)
    fun logout(listener: OAuthListener<Boolean>)
    fun disconnect(listener: OAuthListener<Boolean>)

    interface OAuthListener<D>{
        fun success(data: D)
        fun failure(e:Exception)
    }
}