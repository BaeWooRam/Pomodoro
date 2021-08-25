package com.geekstudio.pomodoro.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.geekstudio.data.notification.RestNotification
import com.geekstudio.data.notification.WorkNotification
import com.geekstudio.data.repository.local.NotificationTimeLocalDataSourceImp
import com.geekstudio.data.repository.local.RecipientLocalDataSourceImp
import com.geekstudio.pomodoro.Config
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.monitor.NotificationMonitorTimerTask
import com.geekstudio.pomodoro.ui.SplashActivity
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import java.util.*

class ForegroundService : Service() {
    private var timer = Timer()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            startForegroundService()
        }

        return START_NOT_STICKY
    }

    private fun startForegroundService() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, SplashActivity::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        var builder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(notificationManager, Config.CHANNEL_ID)


                NotificationCompat.Builder(applicationContext, Config.CHANNEL_ID)
            } else
                NotificationCompat.Builder(applicationContext)


        builder.setSmallIcon(R.drawable.ic_tomato)
            .setContentTitle(getString(R.string.notification_foreground_title))
            .setContentText(getString(R.string.notification_foreground_content))
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        startForeground(Config.NOTIFICATION_FOREGROUND_ID, builder.build())
        initNotificationTimer()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(timer != null)
            timer.cancel()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(manager: NotificationManager, id: String) {
        var channel = NotificationChannel(
            id,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )

        channel.description = getString(R.string.notification_channel_description)
        channel.enableLights(true)
        channel.lightColor = Color.GREEN

        manager.createNotificationChannel(channel)
    }

    private fun initNotificationTimer(){
        if(timer == null)
            timer = Timer()

        //타이머 시작
        timer.schedule(
            NotificationMonitorTimerTask(
                baseContext,
                inject<NotificationTimeLocalDataSourceImp>().value,
                inject<RecipientLocalDataSourceImp>().value,
                getRestNotification(),
                getWorkNotification()
            ),
            0L,
            Config.CONNECTION_MONITOR_PERIOD
        )
    }

    private fun getRestNotification(): RestNotification {
        return object : RestNotification(this@ForegroundService) {
            override fun getNotificationTarget(): NotificationTarget {
                return NotificationTarget(
                    Config.CHANNEL_ID,
                    getString(R.string.notification_channel_name),
                    getString(R.string.notification_channel_description)
                )
            }

            override fun getNotificationData(): NotificationData {
                return NotificationData(
                    getString(R.string.notification_rest_time_title),
                    getString(R.string.notification_rest_time_content),
                    R.drawable.ic_tomato,
                    Config.NOTIFICATION_ID,
                    Intent(this@ForegroundService, SplashActivity::class.java)
                )
            }
        }
    }

    private fun getWorkNotification(): WorkNotification {
        return object : WorkNotification(this@ForegroundService) {
            override fun getNotificationTarget(): NotificationTarget {
                return NotificationTarget(
                    Config.CHANNEL_ID,
                    getString(R.string.notification_channel_name),
                    getString(R.string.notification_channel_description)
                )
            }

            override fun getNotificationData(): NotificationData {
                return NotificationData(
                    getString(R.string.notification_time_title),
                    getString(R.string.notification_time_content),
                    R.drawable.ic_tomato,
                    Config.NOTIFICATION_ID,
                    Intent(this@ForegroundService, SplashActivity::class.java)
                )
            }
        }
    }
}