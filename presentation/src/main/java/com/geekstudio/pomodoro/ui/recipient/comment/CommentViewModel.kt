package com.geekstudio.pomodoro.ui.recipient.comment

import android.content.Context
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.data.repository.local.RecipientLocalDataSourceImp
import com.geekstudio.data.listener.TaskCompleteListener
import com.geekstudio.data.listener.TaskListener
import com.geekstudio.data.repository.local.NotificationTimeLocalDataSourceImp
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.lang.Exception

class CommentViewModel(
    private val context: Context,
    private val notificationTimeLocalDataSourceImp: NotificationTimeLocalDataSourceImp
) : BaseViewModel() {

    fun getNotificationWorkContent():String {
       return notificationTimeLocalDataSourceImp.getNotificationWorkContent() ?: context.getString(R.string.notification_time_content)
    }

    fun getNotificationRestContent():String {
        return notificationTimeLocalDataSourceImp.getNotificationRestContent() ?: context.getString(R.string.notification_rest_time_content)
    }

    fun setNotificationComment(workComment: String, restComment: String) {
        if (!workComment.isNullOrEmpty())
            notificationTimeLocalDataSourceImp.setNotificationWorkContent(workComment)

        if (!restComment.isNullOrEmpty())
            notificationTimeLocalDataSourceImp.setNotificationRestContent(restComment)
    }
}