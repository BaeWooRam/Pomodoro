package com.geekstudio.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RecipientEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val sendId:String,
    val type:Type
){
    enum class Type{
        Kakao, Sms
    }
}
