package com.example.verificacodiceleadtech.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.verificacodiceleadtech.repository.dao.CodeEntryDao
import com.example.verificacodiceleadtech.repository.entity.CodeEntry

@Database(entities = [CodeEntry::class], version = 2)
abstract class CodeDatabase : RoomDatabase() {
    abstract fun codeEntryDao(): CodeEntryDao
}
