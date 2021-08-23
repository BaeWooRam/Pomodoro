package com.geekstudio.pomodoro.ui.recipient.list

import android.os.Bundle
import com.geekstudio.pomodoro.databinding.ActivityRecipientBinding
import com.geekstudio.pomodoro.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipientActivity : BaseActivity() {
    lateinit var binding: ActivityRecipientBinding
    private val viewModel by viewModel<RecipientViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipientBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    companion object {
        const val TAG = "SplashActivity"
        const val SPLASH_DISPLAY_LENGTH = 3000L
    }
}