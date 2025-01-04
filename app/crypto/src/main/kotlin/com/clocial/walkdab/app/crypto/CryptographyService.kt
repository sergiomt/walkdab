package com.clocial.walkdab.app.crypto

interface CryptographyService {

    fun cypher(): String

    fun encryptWithKey(dataToEncrypt: ByteArray, associatedData: ByteArray): ByteArray

    fun decryptWithKey(dataToDecrypt: ByteArray, associatedData: ByteArray): ByteArray

    fun encryptWithPasword(dataToEncrypt: ByteArray, password: String): ByteArray

    fun decryptWithPassword(dataToDecrypt: ByteArray, password: String): ByteArray

    companion object {
        const val NO_CYPHER = "NO_CYPHER"
        const val PB_CYPHER = "SHA256"
    }
}