package com.example.verificacodiceleadtech.repository

import androidx.lifecycle.LiveData
import com.example.verificacodiceleadtech.repository.dao.CodeEntryDao
import com.example.verificacodiceleadtech.repository.entity.CodeEntry

class CodeRepository(private val codeDao: CodeEntryDao) {

    suspend fun insert(codeEntry: CodeEntry) {
        codeDao.insert(codeEntry)
    }

    fun getAllCodeEntries(): LiveData<List<CodeEntry>> {
        return codeDao.getAllCodeEntries()
    }
}
