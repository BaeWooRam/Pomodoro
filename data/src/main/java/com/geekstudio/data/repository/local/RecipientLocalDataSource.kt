package com.geekstudio.data.repository.local

import com.geekstudio.data.db.RecipientEntity


interface RecipientLocalDataSource {
    suspend fun insertRecipient(recipient: RecipientEntity)
    suspend fun getAllRecipient(): List<RecipientEntity>
    suspend fun getAllRecipient(type:RecipientEntity.Type): List<RecipientEntity>
    suspend fun getSearchRecipientFromName(name: String): List<RecipientEntity>
    suspend fun getSearchRecipientFromSendId(phoneNumber: String): List<RecipientEntity>
    suspend fun deleteAllRecipient()
    suspend fun deleteRecipient(recipient: RecipientEntity)
    suspend fun isExist(sendId: String): Boolean
    suspend fun isExist(): Boolean
}