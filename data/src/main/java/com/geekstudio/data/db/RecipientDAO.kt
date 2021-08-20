package com.geekstudio.data.db

import androidx.room.*
import java.util.*

@Dao
interface RecipientDAO {
    @Query("SELECT * FROM RecipientEntity Order by name")
    fun getAllRecipient(): List<RecipientEntity>

    @Query("SELECT * FROM RecipientEntity WHERE id IN (:id)")
    fun getRecipientFromId(id: Int): List<RecipientEntity>

    @Query("SELECT * FROM RecipientEntity WHERE phoneNumber IN (:phoneNumber)")
    fun getRecipientFromPhoneNumber(phoneNumber: String): List<RecipientEntity>?

    @Query("SELECT * FROM RecipientEntity WHERE name IN (:name)")
    fun getRecipientFromName(name: String): List<RecipientEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: RecipientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg entity: RecipientEntity)

    @Query("SELECT EXISTS(SELECT * FROM RecipientEntity)")
    fun isExist(): Boolean

    @Delete
    fun delete(entity: RecipientEntity)

    @Query("DELETE FROM RecipientEntity")
    fun deleteAllRecipient()
}