package com.geekstudio.pomodoro.listener

import java.lang.Exception

interface TaskListener<R> {
    fun onSuccess(result:R)
    fun onFailure(e:Exception)
}