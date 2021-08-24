package com.geekstudio.data.repository.local

import com.geekstudio.data.contacts.Contacts
import com.geekstudio.entity.NotificationTime

interface ContactLocalDataSource {
    fun getContactList(): List<Contacts>
}