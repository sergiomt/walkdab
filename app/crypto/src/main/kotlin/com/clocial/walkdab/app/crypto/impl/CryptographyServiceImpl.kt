package com.clocial.walkdab.app.crypto.impl

import com.clocial.walkdab.app.crypto.CryptographyService
import com.clocial.walkdab.app.crypto.aes.AESCBCPKCS5Padding
import com.clocial.walkdab.app.crypto.pbkdf2.PBKDF2Impl
import com.clocial.walkdab.app.crypto.pbkdf2.PBKDF2Parameters

class CryptographyServiceImpl : CryptographyService {

    // val salt = byteArrayOf(115,121,37,49,52,64,76,87) // SecureRandom.getInstance("SHA1PRNG").nextBytes(salt)

    val pbkdf2 = PBKDF2Impl(8, 2000, PBKDF2Parameters())

    override fun cypher(): String {
        return "AES/CBC/PKCS5Padding"
    }

    override fun encryptWithKey(dataToEncrypt: ByteArray, associatedData: ByteArray): ByteArray {
        throw RuntimeException("Not implemented")
    }

    override fun decryptWithKey(dataToDecrypt: ByteArray, associatedData: ByteArray): ByteArray {
        throw RuntimeException("Not implemented")
    }

    override fun encryptWithPasword(dataToEncrypt: ByteArray, password: String): ByteArray {
        val keyBytes = pbkdf2.deriveKey(password)
        val aesCustom = AESCBCPKCS5Padding(keyBytes)
        return aesCustom.encrypt(dataToEncrypt)
    }

    override fun decryptWithPassword(dataToDecrypt: ByteArray, password: String): ByteArray {
        val keyBytes = pbkdf2.deriveKey(password)
        val aesCustom = AESCBCPKCS5Padding(keyBytes)
        return aesCustom.decrypt(dataToDecrypt)
    }

}