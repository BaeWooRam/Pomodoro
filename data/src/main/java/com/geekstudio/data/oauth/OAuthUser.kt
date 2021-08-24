package com.geekstudio.data.oauth

open class OAuthUser(
    val uuid:String,
    val name:String,
    val email:String) {
    constructor():this("","","")

    override fun toString(): String {
        return "User(uuid='$uuid', name='$name', email='$email')"
    }
}