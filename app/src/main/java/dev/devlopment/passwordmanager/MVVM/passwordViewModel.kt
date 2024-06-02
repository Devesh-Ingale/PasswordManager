import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dev.devlopment.passwordmanager.Crypto.CryptoManager
import dev.devlopment.passwordmanager.MVVM.PasswordRepository
import dev.devlopment.passwordmanager.Room.AppDatabase
import dev.devlopment.passwordmanager.Room.Password
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class PasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PasswordRepository
    val allPasswords: LiveData<List<Password>>
    private val cryptoManager = CryptoManager()

    init {
        val passwordDao = AppDatabase.getDatabase(application).passwordDao()
        repository = PasswordRepository(passwordDao)
        allPasswords = repository.allPasswords
    }

    fun insert(password: Password) = viewModelScope.launch {
        val encryptedPassword = encryptPassword(password.password)
        repository.insert(password.copy(password = encryptedPassword))
    }

    fun update(password: Password) = viewModelScope.launch {
        val encryptedPassword = encryptPassword(password.password)
        repository.update(password.copy(password = encryptedPassword))
    }

    fun delete(password: Password) = viewModelScope.launch {
        repository.delete(password)
    }

    private fun encryptPassword(password: String): String {
        val outputStream = ByteArrayOutputStream()
        cryptoManager.encrypt(password.toByteArray(), outputStream)
        return outputStream.toByteArray().joinToString(separator = ",") { it.toString() }
    }

    fun decryptPassword(encryptedPassword: String): String {
        val byteArray = encryptedPassword.split(",").map { it.toByte() }.toByteArray()
        val inputStream = ByteArrayInputStream(byteArray)
        return String(cryptoManager.decrypt(inputStream))
    }
}
