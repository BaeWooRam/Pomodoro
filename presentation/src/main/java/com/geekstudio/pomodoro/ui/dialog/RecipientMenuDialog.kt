package com.geekstudio.pomodoro.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.databinding.DialogRecipientMenuBinding

class RecipientMenuDialog(
    context: Context,
    private val listener: MenuListener
) : Dialog(context), View.OnClickListener {
    lateinit var binding: DialogRecipientMenuBinding
    var target: RecipientEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRecipientMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvDelete.setOnClickListener(this@RecipientMenuDialog)
        settingDialog()
        settingLayout()
    }

    private fun settingLayout() {
        //window 세팅
        window?.apply {
            setGravity(Gravity.CENTER)
            attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            attributes.dimAmount = 0.5f

            // Dialog 사이즈 조절 하기
            /*attributes.width = WindowManager.LayoutParams.WRAP_CONTENT
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT*/
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun settingDialog() {
        setCancelable(true)
    }

    interface MenuListener {
        fun onDelete(target: RecipientEntity)
        fun onModify(target: RecipientEntity)
    }

    override fun onClick(v: View?) {
        if (target == null) {
            Log.d(javaClass.simpleName, "onClick() target is null")
            return
        }

        when(v?.id){
            R.id.tvDelete ->  listener.onDelete(target!!)
            R.id.tvModify ->  listener.onModify(target!!)
        }

        dismiss()
    }
}