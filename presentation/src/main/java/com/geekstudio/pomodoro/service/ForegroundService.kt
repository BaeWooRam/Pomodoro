package com.geekstudio.pomodoro.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.geekstudio.pomodoro.Config
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.ui.SplashActivity

class ForegroundService:Service() {

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
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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


        builder.setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getString(R.string.notification_foreground_title))
            .setContentText(getString(R.string.notification_foreground_content))
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        startForeground(Config.NOTIFICATION_FOREGROUND_ID, builder.build())
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

}