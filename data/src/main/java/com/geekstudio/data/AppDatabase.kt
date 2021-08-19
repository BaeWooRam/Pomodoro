package com.geekstudio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.geekstudio.data.db.RecipientDAO
import com.geekstudio.data.db.RecipientEntity

@Database(entities = [RecipientEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object{
            fun create(context: Context, name:String): AppDatabase {
                return Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    name
                ).build()
            }
    }

    abstract fun getSensorDataDao(): RecipientDAO
}