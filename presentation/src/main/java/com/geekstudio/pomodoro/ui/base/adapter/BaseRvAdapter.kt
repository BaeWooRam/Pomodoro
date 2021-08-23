package com.geekstudio.pomodoro.ui.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 기본 Recycler View Adapter
 *
 * @author 배우람
 */
abstract class BaseRvAdapter<ViewHolder:RecyclerView.ViewHolder,Type>(protected val context: Context):
    RecyclerView.Adapter<ViewHolder>(), BaseAdapter<Type> {
    enum class Type(val value:Int){
        Header(0), Item(1), Footer(2)
    }

    protected var itemList = ArrayList<Type>()
    protected val inflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var onItemCountListener: OnItemCountListener? = null
    var isLoadingAdded = false


    override fun addItem(item: Type) {
        itemList.add(0,item)

        notifyDataSetChanged()
    }

    override fun setItemList(itemList: List<Type>) {
        for(item in itemList)
            this.itemList.add(item)

        notifyDataSetChanged()
    }

    override fun clearItemList() {
        itemList.clear()
        notifyDataSetChanged()
    }

    class LoadingViewHolder(

        itemView: View
    ) : RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)

    class FooterViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)

    /**
     * 아이템 갯수 관련 이벤트 호출
     */
    interface OnItemCountListener{
        /**
         * 현재 아이템이 없을때
         */
        fun onEmpty()

        /**
         * 현재 아이템이 존재할 때
         */
        fun onExists(count:Int)
    }
}