package com.geekstudio.pomodoro

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialog
import com.geekstudio.pomodoro.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppApplication : Application() {
    private var progressDialog: AppCompatDialog? = null

    override fun onCreate() {
        super.onCreate()
        instance = this@AppApplication

        initKoin()
    }

    private fun initKoin(){
        startKoin{
            androidLogger()
            androidContext(this@AppApplication)
            modules(appModule)
        }
    }

    fun progressON(activity: Activity?) {
        if (activity == null || activity.isFinishing)
            return

        showProgress(
            activity,
            R.layout.progress_loading
        )
    }

    private fun showProgress(activity: Activity?, @LayoutRes layoutID: Int) {
        progressOFF()

        progressDialog = AppCompatDialog(activity)
        progressDialog?.setCancelable(false)
        progressDialog?.setContentView(layoutID)

        val window = progressDialog?.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        progressDialog?.show()
    }

    fun progressOFF() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    companion object{
        private var instance: AppApplication? = null

        fun getGlobalApplicationContext(): AppApplication {
            checkNotNull(instance) { "This Application does not inherit GlobalApplication" }
            return instance!!
        }
    }
}