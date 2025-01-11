package com.clocial.walkdab.app.crypto.aes

import kotlin.Throws

interface SymmetricCiphering {

    open fun getBlockSize(): Int

    @Throws(IllegalArgumentException::class)
    fun init(decrypting: Boolean, key: ByteArray)

    fun encryptBlock(input: ByteArray, inOff: Int, output: ByteArray, outOff: Int)

    fun decryptBlock(input: ByteArray, inOff: Int, output: ByteArray, outOff: Int)
}
