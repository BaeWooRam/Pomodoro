package com.geekstudio.pomodoro.mapper

import com.geekstudio.data.contacts.Contacts
import com.geekstudio.pomodoro.dto.ContactsDto

//Contact 관련
fun convertContactsToDto(contacts: Contacts):ContactsDto = ContactsDto(contacts.name, contacts.phoneNumber)
fun convertDtoToContacts(dto: ContactsDto):Contacts = Contacts(dto.name, dto.phoneNumber)