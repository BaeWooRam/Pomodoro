package com.geekstudio.pomodoro.ui.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import com.geekstudio.entity.NotificationTime
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.databinding.ActivityMainBinding
import com.geekstudio.pomodoro.permission.Permission
import com.geekstudio.pomodoro.service.ForegroundService
import com.geekstudio.pomodoro.ui.base.BaseActivity
import com.geekstudio.pomodoro.ui.base.BasePermissionActivity
import com.geekstudio.pomodoro.ui.recipient.list.RecipientActivity
import com.geekstudio.pomodoro.util.ActivityUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BasePermissionActivity(), NumberPicker.OnValueChangeListener, View.OnClickListener{
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()
    private var serviceIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initService()
        executeCheckPermission()
    }

    override fun getPermissionListener(): Permission.PermissionListener {
        return object :Permission.PermissionListener{
            override fun onGrantedPermission() {
            }

            override fun onDenyPermission(denyPermissions: Array<String>) {
                showMessage(getString(R.string.error_deny_permission))
                finish()
            }
        }
    }

    override fun getCheckPermission(): Array<String> {
        return arrayOf(Manifest.permission.SEND_SMS)
    }

    private fun initService() {
        serviceIntent = Intent(this@MainActivity, ForegroundService::class.java).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(this)
            } else {
                startService(this)
            }
        }
    }

    private fun initView() {
        //FAB 클릭 리스너 초기화
        binding.fabAddRecipient.setOnClickListener(this@MainActivity)

        val notificationTime = viewModel.getNotificationTime()

        //NumberPick 초기화
        binding.npHour.run {
            minValue = 0
            maxValue = 24
            value = notificationTime.hour
            setOnValueChangedListener(this@MainActivity)
        }

        binding.npMinute.run {
            minValue = 0
            maxValue = 59
            value = notificationTime.minute
            setOnValueChangedListener(this@MainActivity)
        }

        binding.npSecond.run {
            minValue = 0
            maxValue = 59
            value = notificationTime.second
            setOnValueChangedListener(this@MainActivity)
        }

        val notificationRestTime = viewModel.getNotificationRestTime()

        //NumberPick 초기화
        binding.npRestMinute.run {
            minValue = 0
            maxValue = 59
            value = notificationRestTime.minute
            setOnValueChangedListener(this@MainActivity)
        }
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
        when (picker?.id) {
            R.id.npHour, R.id.npMinute, R.id.npSecond -> {
                val notificationTime = NotificationTime(
                    binding.npHour.value,
                    binding.npMinute.value,
                    binding.npSecond.value
                )

                Log.d(javaClass.simpleName, "notificationTime = ${notificationTime.toStringHourMinuteSecond()}")
                viewModel.setNotificationTime(notificationTime)
            }

            R.id.npRestMinute -> {
                val notificationRestTime = NotificationTime(0, binding.npRestMinute.value, 0)

                Log.d(javaClass.simpleName, "notificationRestTime = ${notificationRestTime.toStringHourMinuteSecond()}")
                viewModel.setNotificationRestTime(notificationRestTime)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(serviceIntent != null)
            stopService(serviceIntent)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fabAddRecipient -> ActivityUtil.openActivity(this@MainActivity, RecipientActivity::class.java)
        }
    }
}