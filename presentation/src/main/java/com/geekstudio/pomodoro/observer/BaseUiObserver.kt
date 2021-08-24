package com.geekstudio.pomodoro.observer

import android.os.Bundle

interface BaseUiObserver {
    enum class UiType{
        Contacts
    }

    fun getType(): UiType
    fun update(data: Bundle?)
}