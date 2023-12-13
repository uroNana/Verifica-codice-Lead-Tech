package com.example.verificacodiceleadtech.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verificacodiceleadtech.R
import com.example.verificacodiceleadtech.repository.CodeRepository
import com.example.verificacodiceleadtech.repository.entity.CodeEntry
import kotlinx.coroutines.launch
class HomeViewModel(private val codeRepository: CodeRepository) : ViewModel() {

    private val _scannedCodeDetails = MutableLiveData<ScannedCodeDetails>()
    val scannedCodeDetails: LiveData<ScannedCodeDetails>
        get() = _scannedCodeDetails

    fun processScannedCode(inputCode: String) {
        viewModelScope.launch {
            val isCodeValid = codeCheckFunction(inputCode)
            saveScannedCodeToDatabase(inputCode, isCodeValid)
            val messageResId = if (isCodeValid) R.string.valid else R.string.not_valid
            val imageResId = if (isCodeValid) R.drawable.baseline_check else R.drawable.baseline_clear
            val scannedCodeDetails = ScannedCodeDetails(inputCode, messageResId, imageResId)
            _scannedCodeDetails.value = scannedCodeDetails
        }
    }

    private suspend fun saveScannedCodeToDatabase(code: String, isValid: Boolean) {
        val scannedCode = CodeEntry(code = code, isValid = isValid)
        codeRepository.insert(scannedCode)
    }

    private fun codeCheckFunction(inputCode: String): Boolean {
        val cleanedCode = inputCode.replace("[\\-–]".toRegex(), "")
        if (cleanedCode.length != 10) {
            return false
        }
        var result = 0
        for (i in 0 until 10) {
            val char = cleanedCode[i]
            val value = if (char == 'X') 10 else Character.getNumericValue(char)
            result += value * (10 - i)
        }
        return result % 11 == 0
    }
}



