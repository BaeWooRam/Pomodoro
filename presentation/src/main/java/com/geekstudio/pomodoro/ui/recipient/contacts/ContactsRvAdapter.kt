package com.geekstudio.pomodoro.ui.recipient.contacts

import android.content.Context
import android.view.ViewGroup
import com.geekstudio.data.contacts.Contacts
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.pomodoro.ui.base.adapter.BaseRvAdapter
import com.geekstudio.pomodoro.ui.recipient.RecipientComponentViewHolder

class ContactsRvAdapter(context: Context):BaseRvAdapter<RecipientComponentViewHolder, Contacts>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipientComponentViewHolder {
       return RecipientComponentViewHolder.RecipientContactsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecipientComponentViewHolder, position: Int) {
        val target = itemList[position]

        if(holder is RecipientComponentViewHolder.RecipientContactsViewHolder)
            holder.bind(target)
    }

    override fun getItemCount(): Int = itemList.size
}