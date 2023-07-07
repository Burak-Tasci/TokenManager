package com.tsci.jetpackxsecurity

import javax.inject.Inject

/**
 * Saves the token to encrypted file with help of [EncryptionManager].
 * [EncryptionManager] handles all security processes. [TokenManager]
 * manages the accessing token methods.
 *
 *
 *      @Inject
 *      lateinit var tokenManager: TokenManager
 *
 *      ...
 *
 *      tokenManager.setToken("tokentokentoken")
 *
 *      val token = tokenManager.getToken()
 * Created by tasci on 7.07.2023.
 */
class TokenManager @Inject constructor(
    private val encryptionManager: EncryptionManager
) {

    /**
     * The token retrieved from encrypted file
     */
    private var cachedToken: String = ""

    /**
     * Gets the token from encrypted [file][FILE]. Checks if
     * [cachedToken] is empty, if it is not returns the [cachedToken].
     *
     * @return token
     */
    fun getToken(): String {
        return cachedToken.ifEmpty {
            cachedToken = encryptionManager.read(FILE)
            cachedToken
        }
    }

    /**
     * Writes the [token] to encrypted [file][FILE]. Sets the [token]
     * to the [cachedToken] variable.
     */
    fun setToken(token: String) {
        encryptionManager.write(FILE, token)
        cachedToken = token
    }

    private companion object {

        private const val CAESAR_CIPHER_KEY = 15

        val FILE =
            "tokens".toCharArray().map { it + CAESAR_CIPHER_KEY }.joinToString(separator = "")
    }
}