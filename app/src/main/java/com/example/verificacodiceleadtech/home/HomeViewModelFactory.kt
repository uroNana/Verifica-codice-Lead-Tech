package com.example.verificacodiceleadtech.home



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.verificacodiceleadtech.repository.CodeRepository

class HomeViewModelFactory(private val codeRepository: CodeRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(codeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
