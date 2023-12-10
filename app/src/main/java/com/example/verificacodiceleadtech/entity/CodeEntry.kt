package com.example.verificacodiceleadtech.entity

import androidx.room.Entity

@Entity(primaryKeys = ["code"])
data class CodeEntry(
    val id: Long = 0,
    val code: String,
    val isValid: Boolean
)

