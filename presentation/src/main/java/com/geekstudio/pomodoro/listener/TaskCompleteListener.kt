package com.geekstudio.pomodoro.listener

interface TaskCompleteListener {
    fun onSuccess()
    fun onFailure(e:Exception)
}