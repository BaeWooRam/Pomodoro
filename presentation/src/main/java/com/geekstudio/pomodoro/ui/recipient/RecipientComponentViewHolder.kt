package com.geekstudio.pomodoro.ui.recipient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekstudio.pomodoro.R

sealed class RecipientComponentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     *
     */
    class RecipientKakaoViewHolder(
        parent: ViewGroup
    ) : RecipientComponentViewHolder(inflate(parent, R.layout.item_recipient_kakao)), View.OnClickListener{
        override fun onClick(v: View?) {

        }
    }

    /**
     *
     */
    class RecipientSmsViewHolder(
        parent: ViewGroup
    ) : RecipientComponentViewHolder(inflate(parent, R.layout.item_recipient_sms)), View.OnClickListener{
        override fun onClick(v: View?) {

        }
    }

    /**
     *
     */
    class RecipientListViewHolder(
        parent: ViewGroup
    ) : RecipientComponentViewHolder(inflate(parent, R.layout.item_recipient_list)), View.OnClickListener{
        override fun onClick(v: View?) {

        }
    }

    companion object {
        private fun inflate(parent: ViewGroup, layout: Int): View {
            return LayoutInflater.from(parent.context).inflate(layout, parent, false)
        }
    }
}