package com.geekstudio.pomodoro.mapper

import com.geekstudio.data.contacts.Contacts
import com.geekstudio.data.db.RecipientEntity
import com.geekstudio.pomodoro.dto.ContactsDto
import com.geekstudio.pomodoro.dto.RecipientDto

//Contact 관련
fun convertContactsToDto(contacts: Contacts): ContactsDto =
    ContactsDto(contacts.name, contacts.phoneNumber)
fun convertDtoToContacts(dto: ContactsDto): Contacts = Contacts(dto.name, dto.phoneNumber)

//Recipient 관련
fun convertRecipientEntityToDto(recipientEntity: RecipientEntity): RecipientDto = RecipientDto(
    recipientEntity.id,
    recipientEntity.name,
    recipientEntity.sendId,
    recipientEntity.type
)
fun convertDtoToRecipientEntity(dto: RecipientDto): RecipientEntity = RecipientEntity(
    dto.id,
    dto.name,
    dto.sendId,
    dto.type
)

