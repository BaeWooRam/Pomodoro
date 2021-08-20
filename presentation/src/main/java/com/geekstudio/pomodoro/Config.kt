package com.geekstudio.pomodoro

object Config {
    //Monitor
    const val CONNECTION_MONITOR_PERIOD = 1000L
    const val CONNECTION_MONITOR_TIME = CONNECTION_MONITOR_PERIOD.toInt() / 1000

    //Notification
    const val CHANNEL_ID = "pomodoro_notification_channel"
    const val CHANNEL_NAME = "pomodoro_channel"
    const val CHANNEL_DESCRIPTION = "pomodoro notification etc."

    const val NOTIFICATION_FOREGROUND_ID = 1
    const val NOTIFICATION_ID = 1000
}