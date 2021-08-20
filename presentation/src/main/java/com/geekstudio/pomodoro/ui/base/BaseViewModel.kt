package com.geekstudio.pomodoro.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


open class BaseViewModel : ViewModel() {
    protected val scope = CoroutineScope(Dispatchers.Default)
}