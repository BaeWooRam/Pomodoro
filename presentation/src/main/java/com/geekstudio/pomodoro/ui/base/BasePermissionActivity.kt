package com.geekstudio.pomodoro.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.geekstudio.pomodoro.permission.Permission
import com.geekstudio.pomodoro.permission.PermissionChecker

abstract class BasePermissionActivity :
    BaseActivity {
    constructor(@LayoutRes layout: Int) : super(layout)
    constructor() : super()

    private val tag = javaClass.simpleName
    protected lateinit var mPermissionChecker: PermissionChecker
    private lateinit var mPermissionListener: Permission.PermissionListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //퍼미션 초기화
        mPermissionListener = getPermissionListener()
        mPermissionChecker = PermissionChecker().initPermissionLauncher(this@BasePermissionActivity)

        mPermissionChecker
            .target(this@BasePermissionActivity)
            .setPermissionListener(mPermissionListener)
    }

    /**
     * 권한 설정 결과 리스너
     * @author 배우람
     */
    abstract fun getPermissionListener(): Permission.PermissionListener

    /**
     * 권한 설정할 항목들
     * @author 배우람
     */
    abstract fun getCheckPermission(): Array<String>

    /**
     * 권한 체크 실행
     * @author 배우람
     */
    fun executeCheckPermission() {
        mPermissionChecker
            .request(getCheckPermission())
            .check()
    }
}