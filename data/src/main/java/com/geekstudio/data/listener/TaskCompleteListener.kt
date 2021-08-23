package com.geekstudio.data.listener

interface TaskCompleteListener {
    fun onSuccess()
    fun onFailure(e:Exception)
}