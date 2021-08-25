package com.geekstudio.data

import android.content.Context

/**
 * 앱 Shared Preference 관리
 * @author 배우람
 */
class AppPreferences(private val context: Context) {

    /**
     * NotificationTime 저장
     * @author 배우람
     */
    fun setNotificationWorkTime(time:String){
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(SHARED_PREFERENCE_WORK_NOTIFICATION_TIME, time)
        editor.commit()
    }

    /**
     * NotificationTime 가져오기
     * @author 배우람
     */
    fun getNotificationWorkTime():String?{
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return editor.getString(SHARED_PREFERENCE_WORK_NOTIFICATION_TIME, null)
    }

    /**
     * NotificationTime 지우기
     * @author 배우람
     */
    fun clearNotificationWorkTime():Boolean{
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.remove(SHARED_PREFERENCE_WORK_NOTIFICATION_TIME)
        return editor.commit()
    }

    /**
     * Notification Rest Time 지우기
     * @author 배우람
     */
    fun clearNotificationRestTime():Boolean{
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.remove(SHARED_PREFERENCE_REST_NOTIFICATION_TIME)
        return editor.commit()
    }

    /**
     * Notification Rest Time 저장
     * @author 배우람
     */
    fun setNotificationRestTime(time:String){
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(SHARED_PREFERENCE_REST_NOTIFICATION_TIME, time)
        editor.commit()
    }

    /**
     * Notification Rest Time 가져오기
     * @author 배우람
     */
    fun getNotificationRestTime():String?{
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return editor.getString(SHARED_PREFERENCE_REST_NOTIFICATION_TIME, null)
    }


    /**
     * Notification Rest Time 지우기
     * @author 배우람
     */
    fun clearNotificationWorkContent():Boolean{
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.remove(SHARED_PREFERENCE_WORK_NOTIFICATION_CONTENT)
        return editor.commit()
    }

    /**
     * Notification Rest Time 저장
     * @author 배우람
     */
    fun setNotificationWorkContent(content:String){
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(SHARED_PREFERENCE_WORK_NOTIFICATION_CONTENT, content)
        editor.commit()
    }

    /**
     * Notification Rest Time 가져오기
     * @author 배우람
     */
    fun getNotificationWorkContent():String?{
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return editor.getString(SHARED_PREFERENCE_WORK_NOTIFICATION_CONTENT, null)
    }


    /**
     * Notification Rest Time 지우기
     * @author 배우람
     */
    fun clearNotificationRestContent():Boolean{
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.remove(SHARED_PREFERENCE_REST_NOTIFICATION_CONTENT)
        return editor.commit()
    }

    /**
     * Notification Rest Time 저장
     * @author 배우람
     */
    fun setNotificationRestContent(content:String){
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(SHARED_PREFERENCE_REST_NOTIFICATION_CONTENT, content)
        editor.commit()
    }

    /**
     * Notification Rest Time 가져오기
     * @author 배우람
     */
    fun getNotificationRestContent():String?{
        val editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return editor.getString(SHARED_PREFERENCE_REST_NOTIFICATION_CONTENT, null)
    }
    companion object{
        const val SHARED_PREFERENCE_NAME = "Pomodoro"
        const val SHARED_PREFERENCE_WORK_NOTIFICATION_TIME = "NotificationWorkTime"
        const val SHARED_PREFERENCE_REST_NOTIFICATION_TIME = "NotificationRestTime"
        const val SHARED_PREFERENCE_WORK_NOTIFICATION_CONTENT = "NotificationWorkContent"
        const val SHARED_PREFERENCE_REST_NOTIFICATION_CONTENT = "NotificationRestContent"
    }
}