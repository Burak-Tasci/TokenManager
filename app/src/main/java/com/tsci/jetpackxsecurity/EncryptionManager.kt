package com.tsci.jetpackxsecurity

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets
import javax.inject.Inject

/**
 * It uses jetpack security library to encrypt files. This library uses Android
 * Keystore system which lets store cryptographic keys in a container. Uses 256-bit
 * AES encryption algorithm as Android recommended. It has abilities to write and
 * read from these files.
 *
 *
 *      @Inject
 *      lateinit var encryptionManager: EncryptionManager
 *
 *      ...
 *
 *      encryptionManager.write("secrets.txt", "tokentokentoken")
 *
 *      val token = encryptionManager.read("secrets.txt")
 *
 * Created by tasci on 7.07.2023.
 */
class EncryptionManager @Inject constructor(
    @ApplicationContext private val application: Context
) {

    /**
     * Key for encryption and decryption processes
     */
    private val mainKey = MasterKey.Builder(application)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    /**
     *  Creates an encrypted file with passed file name inside applications
     *  file directory.
     */
    private val encryptedFile: (String) -> EncryptedFile = {
        EncryptedFile.Builder(application,
            File(application.filesDir, it),
            mainKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    /**
     * Writes [content] to [file][fileName]. Encrypts file before write. Deletes the file if it
     * exists before.
     * @param fileName target file
     * @param content text to write inside file
     */
    fun write(fileName: String, content: String) {
        Log.d(this::class.simpleName, "File deleted: ${deleteIfFileExists(fileName)}")
        encryptedFile(fileName).openFileOutput().apply {
            write(content.toByteArray(StandardCharsets.UTF_8))
            flush()
            close()
        }
    }

    /**
     * Reads [file][fileName]'s content from encrypted file and returns
     * it.
     * @param fileName target file
     * @return target file's content
     */
    fun read(fileName: String): String {
        val inputStream = encryptedFile(fileName).openFileInput()
        val byteArrayOutputStream = ByteArrayOutputStream()
        var nextByte: Int = inputStream.read()
        while (nextByte != -1) {
            byteArrayOutputStream.write(nextByte)
            nextByte = inputStream.read()
        }

        val plaintext: ByteArray = byteArrayOutputStream.toByteArray()
        return plaintext.decodeToString()
    }

    /**
     * Checks if passed file exists in application's file directory.
     * If it exists deletes the file.
     *
     * @param fileName target file
     * @return is delete action successful
     */
    private fun deleteIfFileExists(fileName: String): Boolean{
        val path = "${application.filesDir}/${fileName}"
        val file = File(path)
        if (file.exists()) {
            return file.delete()
        }
        return false
    }

}