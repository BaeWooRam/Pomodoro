package com.geekstudio.pomodoro.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geekstudio.data.db.RecipientEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
class RecipientDto(
    val id: Int,
    val name: String,
    val sendId: String,
    val type: RecipientEntity.Type
) : Parcelable
