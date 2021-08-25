package com.geekstudio.pomodoro.ui.recipient.list

import android.content.Context
import android.view.ViewGroup
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.pomodoro.ui.base.adapter.BaseRvAdapter
import com.geekstudio.pomodoro.ui.recipient.RecipientComponentViewHolder

class RecipientRvAdapter(context: Context):BaseRvAdapter<RecipientComponentViewHolder, RecipientEntity>(context) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipientComponentViewHolder {
        return RecipientComponentViewHolder.RecipientListViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecipientComponentViewHolder, position: Int) {
        val target = itemList[position]

        if(holder is RecipientComponentViewHolder.RecipientListViewHolder)
            holder.bind(target)
    }

    override fun getItemCount(): Int = itemList.size


    fun remove(recipientEntity: RecipientEntity){
        val index = itemList.indexOf(recipientEntity)

        itemList.remove(recipientEntity)
        notifyItemRemoved(index)
    }
}