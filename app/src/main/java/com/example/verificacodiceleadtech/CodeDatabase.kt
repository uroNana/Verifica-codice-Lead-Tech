package com.example.verificacodiceleadtech

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.verificacodiceleadtech.dao.CodeEntryDao
import com.example.verificacodiceleadtech.entity.CodeEntry

@Database(entities = [CodeEntry::class], version = 2)
abstract class CodeDatabase : RoomDatabase() {
    abstract fun codeEntryDao(): CodeEntryDao
}
