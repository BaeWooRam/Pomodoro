package com.geekstudio.pomodoro.permission

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity

interface Permission {
    interface Target{
        fun target(target:AppCompatActivity): Listener
    }

    interface Listener{
        fun setPermissionListener(permissionListener: PermissionListener): Request
    }

    interface Request: Option {
        fun request(permissions:Array<String>): Checker
    }

    interface Option{
        fun setSinglePermissionShowListener(showListener: ShowListener<String>): Request
        fun setMultiPermissionShowListener(showListener: ShowListener<Array<String>>): Request
    }

    interface Checker{
        fun check()
    }

    /**
     * 퍼미션 결과로부터 진행할 콜백 인터페이스
     * @author 배우람
     */
    interface PermissionListener{
        fun onGrantedPermission()
        fun onDenyPermission(denyPermissions: Array<String>)
    }

    /**
     * 퍼미션 결과 보여줄게 있을 경우
     * @author 배우람
     */
    interface ShowListener<PermissionType>{
        fun showRequestPermissionRationale(permissions: PermissionType, launcher: ActivityResultLauncher<PermissionType>)
    }
}