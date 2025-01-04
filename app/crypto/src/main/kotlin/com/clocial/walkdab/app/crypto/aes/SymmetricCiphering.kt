package com.clocial.walkdab.app.crypto.aes

import kotlin.Throws

interface SymmetricCiphering {

    open fun getBlockSize(): Int

    @Throws(IllegalArgumentException::class)
    open fun init(decrypting: Boolean, key: ByteArray)

    open fun encryptBlock(input: ByteArray, inOffset: Int, output: ByteArray, outOffset: Int)

    open fun decryptBlock(input: ByteArray, inOffset: Int, output: ByteArray, outOffset: Int)
}
