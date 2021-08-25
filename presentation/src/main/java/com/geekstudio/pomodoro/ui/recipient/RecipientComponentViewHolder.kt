package com.geekstudio.pomodoro.ui.recipient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekstudio.data.contacts.Contacts
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.pomodoro.R
import com.geekstudio.pomodoro.mapper.convertContactsToDto
import com.geekstudio.pomodoro.mapper.convertRecipientEntityToDto
import com.geekstudio.pomodoro.ui.base.adapter.BaseViewHolder
import com.geekstudio.pomodoro.observer.BaseUiObserver
import com.geekstudio.pomodoro.observer.UiObserverManager

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
    class RecipientContactsViewHolder(
        parent: ViewGroup
    ) : RecipientComponentViewHolder(inflate(parent, R.layout.item_recipient_sms)), View.OnClickListener, BaseViewHolder<Contacts>{
        private val tvPhoneNumber = itemView.findViewById<TextView>(R.id.tvSendId)
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val ivProfile = itemView.findViewById<ImageView>(R.id.ivProfile)
        private var contacts:Contacts? = null

        init {
            itemView.setOnClickListener(this@RecipientContactsViewHolder)
        }

        override fun bind(data: Contacts) {
            this@RecipientContactsViewHolder.contacts = data

            tvName.text = data.name
            tvPhoneNumber.text = data.phoneNumber
        }

        override fun onClick(v: View?) {
            if(contacts == null)
                return

            UiObserverManager.notifyUpdate(BaseUiObserver.UiType.Contacts, Bundle().apply {
                putParcelable(BUNDLE_CONTACTS_DATA, convertContactsToDto(contacts!!))
            })
        }
    }

    /**
     *
     */
    class RecipientListViewHolder(
        parent: ViewGroup
    ) : RecipientComponentViewHolder(inflate(parent, R.layout.item_recipient_list)), View.OnLongClickListener, BaseViewHolder<RecipientEntity>{
        private val tvName = itemView.findViewById<TextView>(R.id.tvName)
        private val tvSendId = itemView.findViewById<TextView>(R.id.tvSendId)
        private val tvRecipientType = itemView.findViewById<TextView>(R.id.tvRecipientType)
        private var recipientEntity:RecipientEntity? = null

        init {
            itemView.setOnLongClickListener(this@RecipientListViewHolder)
        }

        override fun bind(data: RecipientEntity) {
            this@RecipientListViewHolder.recipientEntity = data

            tvName.text = data.name
            tvSendId.text = data.sendId
            tvRecipientType.text = data.type.name
        }

        override fun onLongClick(v: View?): Boolean {
            if(recipientEntity == null)
                return true

            UiObserverManager.notifyUpdate(BaseUiObserver.UiType.Recipient, Bundle().apply {
                putParcelable(BUNDLE_RECIPIENT_DATA, convertRecipientEntityToDto(recipientEntity!!))
            })
            return true
        }
    }

    companion object {
        const val BUNDLE_CONTACTS_DATA ="BundleContactsData"
        const val BUNDLE_RECIPIENT_DATA ="BundleRecipientData"

        private fun inflate(parent: ViewGroup, layout: Int): View {
            return LayoutInflater.from(parent.context).inflate(layout, parent, false)
        }
    }
}