package com.example.verificacodiceleadtech.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.verificacodiceleadtech.repository.entity.CodeEntry

@Dao
interface CodeEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(codeEntry: CodeEntry)

    @Query("SELECT * FROM codeentry ORDER BY id DESC")
    fun getAllCodeEntries(): LiveData<List<CodeEntry>>
}