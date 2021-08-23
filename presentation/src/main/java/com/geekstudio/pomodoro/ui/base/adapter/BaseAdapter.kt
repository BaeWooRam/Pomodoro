package com.geekstudio.pomodoro.ui.base.adapter


interface BaseAdapter<T> {
    fun addItem(item: T)
    fun setItemList(itemList: List<T>)
    fun clearItemList()
}