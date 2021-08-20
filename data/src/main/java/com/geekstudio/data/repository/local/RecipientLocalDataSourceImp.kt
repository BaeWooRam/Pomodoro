package com.geekstudio.data.repository.local

import com.geekstudio.data.db.RecipientDAO
import com.geekstudio.data.db.RecipientEntity

class RecipientLocalDataSourceImp(private val dao: RecipientDAO) : RecipientLocalDataSource {
    override suspend fun insertRecipient(recipient: RecipientEntity) = dao.insert(recipient)

    override suspend fun getAllRecipient(): List<RecipientEntity> = dao.getAllRecipient()

    override suspend fun getSearchRecipientFromName(name: String): List<RecipientEntity>{
        return dao.getRecipientFromName(name) ?: arrayListOf()
    }

    override suspend fun getSearchRecipientFromPhoneNumber(phoneNumber: String): List<RecipientEntity>{
       return dao.getRecipientFromPhoneNumber(phoneNumber) ?: arrayListOf()
    }

    override suspend fun deleteAllRecipient() = dao.deleteAllRecipient()

    override suspend fun deleteRecipient(recipient: RecipientEntity) = dao.delete(recipient)

}