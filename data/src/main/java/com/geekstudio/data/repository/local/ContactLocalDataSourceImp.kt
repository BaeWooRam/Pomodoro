package com.geekstudio.data.repository.local

import com.geekstudio.data.contacts.Contacts
import com.geekstudio.data.contacts.ContactsManager

class ContactLocalDataSourceImp(private val contactsManager: ContactsManager):ContactLocalDataSource {
    override fun getContactList(): List<Contacts> {
        return contactsManager.getContacts()
    }
}