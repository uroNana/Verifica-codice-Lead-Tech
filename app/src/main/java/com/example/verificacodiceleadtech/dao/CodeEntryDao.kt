package com.example.verificacodiceleadtech.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.verificacodiceleadtech.entity.CodeEntry

@Dao
interface CodeEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(codeEntry: CodeEntry)

    @Query("SELECT * FROM codeentry ORDER BY id DESC")
    fun getAllCodeEntries(): LiveData<List<CodeEntry>>
}