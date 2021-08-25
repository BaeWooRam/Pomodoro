package com.geekstudio.pomodoro.module

import android.content.Intent
import com.geekstudio.data.AppDatabase
import com.geekstudio.data.AppPreferences
import com.geekstudio.data.contacts.ContactsManager
import com.geekstudio.data.notification.BaseNotification
import com.geekstudio.data.notification.RestNotification
import com.geekstudio.data.notification.WorkNotification
import com.geekstudio.data.oauth.KakaoLogin
import com.geekstudio.data.repository.local.ContactLocalDataSourceImp
import com.geekstudio.data.repository.local.NotificationTimeLocalDataSourceImp
import com.geekstudio.data.repository.local.RecipientLocalDataSourceImp
import com.geekstudio.data.repository.remote.friend.KakaoFriendRemoteDataSourceImp
import com.geekstudio.pomodoro.Config
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.monitor.NotificationMonitorTimerTask
import com.geekstudio.pomodoro.ui.SplashActivity
import com.geekstudio.pomodoro.ui.main.MainViewModel
import com.geekstudio.pomodoro.ui.recipient.comment.CommentViewModel
import com.geekstudio.pomodoro.ui.recipient.contacts.ContactsViewModel
import com.geekstudio.pomodoro.ui.recipient.list.RecipientViewModel
import com.kakao.sdk.talk.TalkApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        AppDatabase.create(androidContext(), "PomodoroDB")
    }

    single {
        AppPreferences(androidContext())
    }

    single {
        KakaoLogin(androidContext())
    }

    single {
        ContactsManager(androidContext())
    }

    single {
        ContactLocalDataSourceImp(get<ContactsManager>())
    }

    single {
        RecipientLocalDataSourceImp(get<AppDatabase>().getRecipientDao())
    }

    single {
        KakaoFriendRemoteDataSourceImp(TalkApiClient.instance)
    }

    single {
        NotificationTimeLocalDataSourceImp(get())
    }
}

val notificationModule = module {
    single {
        object : RestNotification(androidContext()) {
            override fun getNotificationTarget(): NotificationTarget {
                return NotificationTarget(
                    Config.CHANNEL_ID,
                    androidContext().getString(R.string.notification_channel_name),
                    androidContext().getString(R.string.notification_channel_description)
                )
            }

            override fun getNotificationData(): NotificationData {
                return NotificationData(
                    androidContext().getString(R.string.notification_rest_time_title),
                    androidContext().getString(R.string.notification_rest_time_content),
                    R.drawable.ic_launcher_foreground,
                    Config.NOTIFICATION_ID,
                    Intent(androidContext(), SplashActivity::class.java)
                )
            }
        }
    }

    single {
        object : WorkNotification(androidContext()) {
            override fun getNotificationTarget(): NotificationTarget {
                return NotificationTarget(
                    Config.CHANNEL_ID,
                    androidContext().getString(R.string.notification_channel_name),
                    androidContext().getString(R.string.notification_channel_description)
                )
            }

            override fun getNotificationData(): NotificationData {
                return NotificationData(
                    androidContext().getString(R.string.notification_time_title),
                    androidContext().getString(R.string.notification_time_content),
                    R.drawable.ic_launcher_foreground,
                    Config.NOTIFICATION_ID,
                    Intent(androidContext(), SplashActivity::class.java)
                )
            }
        }
    }
}

val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        RecipientViewModel(get())
    }

    viewModel {
        ContactsViewModel(get(), get())
    }

    viewModel {
        CommentViewModel(androidContext(), get())
    }
}