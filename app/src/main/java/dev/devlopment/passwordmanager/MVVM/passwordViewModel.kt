package dev.devlopment.passwordmanager.MVVM

import android.app.Application
import androidx.lifecycle.*
import dev.devlopment.passwordmanager.Room.AppDatabase
import dev.devlopment.passwordmanager.Room.Password
import kotlinx.coroutines.launch

class PasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PasswordRepository
    val allPasswords: LiveData<List<Password>>

    init {
        val passwordDao = AppDatabase.getDatabase(application).passwordDao()
        repository = PasswordRepository(passwordDao)
        allPasswords = repository.allPasswords
    }

    fun insert(password: Password) = viewModelScope.launch {
        repository.insert(password)
    }

    fun update(password: Password) = viewModelScope.launch {
        repository.update(password)
    }

    fun delete(password: Password) = viewModelScope.launch {
        repository.delete(password)
    }
}
