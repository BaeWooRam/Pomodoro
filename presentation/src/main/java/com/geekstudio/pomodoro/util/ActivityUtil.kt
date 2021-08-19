package com.geekstudio.pomodoro.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 액티비티 관련 유틸리티
 *
 * @author 배우람
 */
object ActivityUtil {

    /**
     * 해당 액티비티를 열다
     * @param flags 설정할 액티비티 관련 플래그들
     * @param bundle 액티비티로 넘길 데이터들
     */
    fun openActivity(context: Context, clazz: Class<*>, flags: Array<Int>?, bundle: Bundle?) {
        val intent = Intent(context, clazz).apply {
            //Flag 설정
            flags?.let {
                for (flag in it)
                    addFlags(flag)
            }
            //Bundle 설정
            bundle?.let {
                putExtras(it)
            }
        }

        context.startActivity(intent)
    }

    /**
     * 해당 액티비티를 열다
     * @param bundle 액티비티로 넘길 데이터들
     */
    fun openActivity(context: Context, clazz: Class<*>, bundle: Bundle) {
        openActivity(context, clazz, null, bundle)
    }

    /**
     * 해당 액티비티를 열다
     * @param flags 설정할 액티비티 관련 플래그들
     */
    fun openActivity(context: Context, clazz: Class<*>, flags: Array<Int>) {
        openActivity(context, clazz, flags, null)
    }

    /**
     * 해당 액티비티를 열다
     */
    fun openActivity(context: Context, clazz: Class<*>) {
        openActivity(context, clazz, null, null)
    }
}