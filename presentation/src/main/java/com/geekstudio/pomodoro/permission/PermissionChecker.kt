package com.geekstudio.pomodoro.permission

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * 앱 권한 체커
 * @author 배우람
 */
class PermissionChecker : Permission.Target, Permission.Listener, Permission.Request {
    private val debugTag: String = javaClass.simpleName
    private var target: AppCompatActivity? = null

    //Event
    private var permissionListener: Permission.PermissionListener? = null
    private var singlePermissionShowListener: Permission.ShowListener<String>? = null
    private var multiPermissionShowListener: Permission.ShowListener<Array<String>>? = null

    //Launch
    private var launchPermission: ActivityResultLauncher<String>? = null
    private var launchMultiplePermissions: ActivityResultLauncher<Array<String>>? = null

    private var permissions: Array<String>? = null
    var isGrantPermission:Boolean = false

    /**
     * 퍼미션 요청을 위한 Launcher 초기화(필수)
     * @author 배우람
     */
    fun initPermissionLauncher(target: AppCompatActivity): PermissionChecker {
        //init single permission launcher
        launchPermission =
            target.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                Log.d(debugTag, "lunchPermission() result = $it")
                if (it){
                    isGrantPermission = true
                    permissionListener!!.onGrantedPermission()
                } else{
                    isGrantPermission = false
                    permissionListener!!.onDenyPermission(permissions!!)
                }
            }


        //init multi permission launcher
        launchMultiplePermissions =
            target.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
                var denyPermissions: ArrayList<String> = ArrayList()

                for (entry in map.entries) {
                    Log.d(
                        debugTag,
                        "lunchMultiplePermissions() result = ${entry.key} = ${entry.value}"
                    )
                    if (!entry.value)
                        denyPermissions.add(entry.key)
                }

                if (denyPermissions.isEmpty()){
                    isGrantPermission = true
                    permissionListener!!.onGrantedPermission()
                } else{
                    isGrantPermission = false
                    permissionListener!!.onDenyPermission(denyPermissions.toTypedArray())
                }
            }

        return this@PermissionChecker
    }

    /**
     * 퍼미션이 하나일 때 Launcher 초기화(선택)
     * @author 배우람
     */
    fun setLaunchSinglePermissions(launcher: ActivityResultLauncher<String>): PermissionChecker {
        this@PermissionChecker.launchPermission = launcher
        return this@PermissionChecker
    }

    /**
     * 퍼미션이 다수일 때, Launcher 초기화(선택)
     * @author 배우람
     */
    fun setLaunchMultiplePermissions(launcher: ActivityResultLauncher<Array<String>>): PermissionChecker {
        this@PermissionChecker.launchMultiplePermissions = launcher
        return this@PermissionChecker
    }

    /**
     * 퍼미션을 요청하기 위한 타깃 설정(필수)
     * @author 배우람
     */
    override fun target(target: AppCompatActivity): Permission.Listener {
        this@PermissionChecker.target = target
        return this@PermissionChecker
    }

    /**
     * 퍼미션에 따른 이벤트 처리 설정(필수)
     * @author 배우람
     */
    override fun setPermissionListener(permissionListener: Permission.PermissionListener): Permission.Request {
        this@PermissionChecker.permissionListener = permissionListener
        return this@PermissionChecker
    }
    
    override fun setSinglePermissionShowListener(showListener: Permission.ShowListener<String>): Permission.Request {
        this@PermissionChecker.singlePermissionShowListener = showListener
        return this@PermissionChecker
    }
    
    override fun setMultiPermissionShowListener(showListener: Permission.ShowListener<Array<String>>): Permission.Request {
        this@PermissionChecker.multiPermissionShowListener = showListener
        return this@PermissionChecker
    }

    /**
     * 요청할 퍼미션 설정(필수)
     * @author 배우람
     */
    override fun request(permissions: Array<String>): Permission.Checker {
        this@PermissionChecker.permissions = permissions
        return Checker()
    }

    inner class Checker : Permission.Checker {
        override fun check() {
            //퍼미션 시작 전 검증 과정
            val verify = checkPermissionVerification()

            if(verify != PermissionVerifyResult.OK) {
                Log.d(debugTag, "checkPermissionVerification() result = ${verify.name}")
                return
            }

            //퍼미션 체크 시작
            when (permissions!!.size) {
                1 -> singlePermissionCheck()
                else -> multiPermissionCheck()
            }
        }

        /**
         * 퍼미션 시작 전 검증 단계 진행
         * @author 배우람
         */
        private fun checkPermissionVerification(): PermissionVerifyResult {
            if (launchMultiplePermissions == null && launchPermission == null)
                return PermissionVerifyResult.NOT_INIT_LAUNCH

            if (target == null)
                return PermissionVerifyResult.NOT_INIT_TARGET

            if (permissions == null)
                return PermissionVerifyResult.NOT_INIT_PERMISSION_DATA

            if (permissions!!.isEmpty())
                return PermissionVerifyResult.EMPTY_PERMISSION_DATA

            if (permissionListener == null)
                return PermissionVerifyResult.NOT_INIT_PERMISSION_LISTENER

            return PermissionVerifyResult.OK
        }

        /**
         * 퍼미션 하나일 때, 퍼미션 요청 진행
         * @author 배우람
         */
        private fun singlePermissionCheck() {
            val requestPermissions = permissions!![0]

            when {
                //퍼미션 모두 허가일 때
                ContextCompat.checkSelfPermission(
                    target!!,
                    requestPermissions
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d(debugTag, "singleExecute() requestPermission = $requestPermissions")
                    isGrantPermission = true
                }

                //퍼미션 거절 시 UI 보여주기
                target!!.shouldShowRequestPermissionRationale(requestPermissions) -> {
                    if (singlePermissionShowListener != null)
                        singlePermissionShowListener?.showRequestPermissionRationale(
                            requestPermissions,
                            launchPermission!!
                        )
                    else
                        launchPermission?.launch(requestPermissions)
                }

                //퍼미션 요청 진행
                else -> {
                    launchPermission?.launch(requestPermissions)
                }
            }
        }

        /**
         * 퍼미션이 다수일 때, 퍼미션 요청 진행
         * @author 배우람
         */
        private fun multiPermissionCheck() {
            val requestPermission = ArrayList<String>()

            //퍼미션 허가 체크
            for (permission in permissions!!) {
                if (ContextCompat.checkSelfPermission(
                        target!!,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission.add(permission)
                }
            }

            Log.d(debugTag, "multiExecute() requestPermission = $requestPermission")

            when {
                //퍼미션 모두 허가일 때
                requestPermission.isEmpty() -> {
                    isGrantPermission = true
                }

                //퍼미션 거절 시 UI 보여주기
                target!!.shouldShowRequestPermissionRationale(requestPermission[0]) -> {
                    if (multiPermissionShowListener != null)
                        multiPermissionShowListener?.showRequestPermissionRationale(
                            permissions!!,
                            launchMultiplePermissions!!
                        )
                    else
                        launchMultiplePermissions?.launch(permissions)
                }

                //퍼미션 요청 진행
                else -> {
                    launchMultiplePermissions?.launch(permissions)
                }
            }
        }
    }
}