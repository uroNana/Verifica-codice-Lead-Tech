package com.example.verificacodiceleadtech.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.verificacodiceleadtech.repository.dao.CodeEntryDao
import com.example.verificacodiceleadtech.repository.entity.CodeEntry

@Database(entities = [CodeEntry::class], version = 2)
abstract class CodeDatabase : RoomDatabase() {
    abstract fun codeEntryDao(): CodeEntryDao

    companion object {
        @Volatile
        private var INSTANCE: CodeDatabase? = null

        fun getDatabase(context: Context): CodeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CodeDatabase::class.java,
                    "app-database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
