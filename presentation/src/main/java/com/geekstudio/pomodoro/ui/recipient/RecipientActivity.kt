package com.geekstudio.pomodoro.ui.recipient

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.databinding.ActivityMainBinding
import com.geekstudio.pomodoro.ui.base.BaseActivity
import com.geekstudio.pomodoro.ui.main.MainActivity
import com.geekstudio.pomodoro.util.ActivityUtil

class RecipientActivity : BaseActivity(R.layout.activity_splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Splash 유지 이후 Handling
        Handler(mainLooper).postDelayed({
            ActivityUtil.openActivity(this@RecipientActivity, MainActivity::class.java)
            finish()
        }, SPLASH_DISPLAY_LENGTH)
    }

    companion object{
        const val TAG = "SplashActivity"
        const val SPLASH_DISPLAY_LENGTH = 3000L
    }
}