package dev.devlopment.passwordmanager.Room


import android.util.Base64
import android.util.Log
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

//object EncryptionUtil {
//    private const val TAG = "EncryptionUtil"
//    private const val ALGORITHM = "AES"
//    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
//    private const val IV_SIZE = 16
//    private const val KEY_SIZE = 256
//    private const val ITERATION_COUNT = 65536
//    private const val SALT_SIZE = 16
//
//    private val password: String = generateRandomPassword(32) // Generate a random password
//
//    fun encrypt(input: String): String? {
//        try {
//            val salt = generateRandomSalt(SALT_SIZE)
//            val key = generateKey(password, salt)
//            val ivParameterSpec = generateRandomIV(IV_SIZE)
//
//            val cipher = Cipher.getInstance(TRANSFORMATION)
//            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec)
//            val encryptedBytes = cipher.doFinal(input.toByteArray())
//
//            val combined = salt + ivParameterSpec.iv + encryptedBytes
//            return Base64.encodeToString(combined, Base64.DEFAULT)
//        } catch (e: Exception) {
//            Log.e(TAG, "Encryption error: ${e.message}", e)
//            return null
//        }
//    }
//
//    fun decrypt(input: String): String? {
//        try {
//            val decodedBytes = Base64.decode(input, Base64.DEFAULT)
//            val salt = decodedBytes.copyOfRange(0, SALT_SIZE)
//            val ivBytes = decodedBytes.copyOfRange(SALT_SIZE, SALT_SIZE + IV_SIZE)
//            val encryptedBytes = decodedBytes.copyOfRange(SALT_SIZE + IV_SIZE, decodedBytes.size)
//
//            val key = generateKey(password, salt)
//            val ivParameterSpec = IvParameterSpec(ivBytes)
//
//            val cipher = Cipher.getInstance(TRANSFORMATION)
//            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)
//            val decryptedBytes = cipher.doFinal(encryptedBytes)
//            return String(decryptedBytes)
//        } catch (e: Exception) {
//            Log.e(TAG, "Decryption error: ${e.message}", e)
//            return null
//        }
//    }
//
//    private fun generateKey(password: String, salt: ByteArray): SecretKeySpec {
//        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
//        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE)
//        val secretKey = factory.generateSecret(spec)
//        return SecretKeySpec(secretKey.encoded, ALGORITHM)
//    }
//
//    private fun generateRandomIV(size: Int): IvParameterSpec {
//        val random = SecureRandom()
//        val ivBytes = ByteArray(size)
//        random.nextBytes(ivBytes)
//        return IvParameterSpec(ivBytes)
//    }
//
//    private fun generateRandomSalt(size: Int): ByteArray {
//        val random = SecureRandom()
//        val saltBytes = ByteArray(size)
//        random.nextBytes(saltBytes)
//        return saltBytes
//    }
//
//    private fun generateRandomPassword(length: Int): String {
//        val charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*()-_=+[]{}|;:,.<>/?"
//        val random = SecureRandom()
//        return (1..length)
//            .map { random.nextInt(charPool.length) }
//            .map(charPool::get)
//            .joinToString("")
//    }
//}
//
