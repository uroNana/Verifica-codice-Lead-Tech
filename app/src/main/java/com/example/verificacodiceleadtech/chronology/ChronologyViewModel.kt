package com.example.verificacodiceleadtech.chronology

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.verificacodiceleadtech.repository.CodeDatabase
import com.example.verificacodiceleadtech.repository.CodeRepository
import com.example.verificacodiceleadtech.repository.entity.CodeEntry
class ChronologyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CodeRepository

    init {
        val codeDao = CodeDatabase.getDatabase(application).codeEntryDao()
        repository = CodeRepository(codeDao)
    }

    fun getAllCodeEntries(): LiveData<List<CodeEntry>> {
        return repository.getAllCodeEntries()
    }
}

