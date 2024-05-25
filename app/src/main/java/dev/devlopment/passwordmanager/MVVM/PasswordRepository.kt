package dev.devlopment.passwordmanager.MVVM

import androidx.compose.material3.Switch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import dev.devlopment.passwordmanager.Room.Password
import dev.devlopment.passwordmanager.Room.PasswordDao

class PasswordRepository(private val passwordDao: PasswordDao) {
    private val _allPasswords = MediatorLiveData<List<Password>>()

    val allPasswords: LiveData<List<Password>> get() = _allPasswords

    init {
        _allPasswords.addSource(passwordDao.getAll()) { entities ->
            _allPasswords.value = entities
        }
    }

    suspend fun insert(password: Password) {
        passwordDao.insert(password)
    }

    suspend fun update(password: Password) {
        passwordDao.update(password)
    }

    suspend fun delete(password: Password) {
        passwordDao.delete(password)
    }
}

