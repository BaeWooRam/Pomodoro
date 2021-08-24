package com.geekstudio.pomodoro.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactsDto(
    val name: String,
    val phoneNumber: String
):Parcelable {
}