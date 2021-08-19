package com.geekstudio.pomodoro.ui.base

import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.geekstudio.pomodoro.AppApplication

open class BaseActivity : AppCompatActivity {
    constructor(@LayoutRes layoutId:Int):super(layoutId)
    constructor():super()

    fun showMessage(msg: String) {
        runOnUiThread {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    fun progressON() {
        AppApplication.getGlobalApplicationContext().progressON(this)
    }

    fun progressOFF() {
        AppApplication.getGlobalApplicationContext().progressOFF()
    }
}