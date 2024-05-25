package dev.devlopment.passwordmanager.Room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PasswordDao {
    @Query("SELECT * FROM passwords")
    fun getAll(): LiveData<List<Password>>

    @Insert
    suspend fun insert(password: Password)

    @Update
    suspend fun update(password: Password)

    @Delete
    suspend fun delete(password: Password)
}


