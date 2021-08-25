package com.geekstudio.pomodoro.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.geekstudio.pomodoro.R


class NoItemView: ConstraintLayout, View.OnClickListener {
    private var ivImage: ImageView? = null
    private var tvContent: TextView? = null
    private var btHandling: Button? = null
    private var view:View? = null

    constructor(context: Context): super(context){
        initView()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):   super(context, attrs, defStyleAttr){
        initView()
    }


    private fun initView() {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = li.inflate(R.layout.layout_no_item, this, true)

        tvContent = findViewById<TextView>(R.id.tvContent)
        ivImage = findViewById<ImageView>(R.id.ivImage)
        btHandling = findViewById<Button>(R.id.btHandling).apply {
            setOnClickListener(this@NoItemView)
        }
    }

    fun setImage(@DrawableRes resId:Int){
        ivImage?.setBackgroundResource(resId)
    }

    fun setContent(content: String) {
        tvContent?.text = content
    }

    fun setContent(@StringRes resID: Int) {
        tvContent?.setText(resID)
    }

    fun setButtonName(@StringRes resID: Int) {
        btHandling?.setText(resID)
    }

    fun setButtonName(buttonName: String) {
        btHandling?.setText(buttonName)
    }

    fun showButton(isShow:Boolean){
        btHandling?.visibility = if(isShow) View.VISIBLE else View.GONE
    }

    fun setting(noItemSetting: NoItemSetting){
        setImage(noItemSetting.icon)
        setContent(noItemSetting.content)

        //버튼
        if(!noItemSetting.buttonName.isNullOrEmpty()){
            setButtonName(noItemSetting.buttonName)
            onNoItemButtonClickListener = noItemSetting.clickListener
            showButton(true)
        }else
            showButton(false)
    }

    /**
     * No Item 세팅
     */
    data class NoItemSetting(
        @DrawableRes val icon: Int,
        val content: String,
        val buttonName:String?,
        val clickListener: NoItemView.OnNoItemButtonClickListener?
    )


    /**
     * No Item 관련 유도 버튼을 눌렀을때 사용
     */
    interface OnNoItemButtonClickListener{
        /**
         * No Item 관련 버튼 Click 시
         */
        fun onButtonClick()
    }

    var onNoItemButtonClickListener: OnNoItemButtonClickListener? = null

    override fun onClick(v: View?) {
        onNoItemButtonClickListener?.onButtonClick()
    }
}