package com.clocial.walkdab.app.crypto.aes

import kotlin.Throws

class CBCBlockChain(cipher: SymmetricCiphering) {

    private val embeddedCipher: SymmetricCiphering = cipher

    private val blockSize = 16

    private var r: ByteArray = ByteArray(this.blockSize)

    private val k: ByteArray = ByteArray(this.blockSize)

    private var rSave: ByteArray? = null

    var iv: ByteArray? = null

    @Throws(IllegalArgumentException::class, ArrayIndexOutOfBoundsException::class)
    fun encryptFinal(plain: ByteArray, plainOffset: Int, plainLen: Int, cipher: ByteArray, cipherOffset: Int): Int {
        return this.encrypt(plain, plainOffset, plainLen, cipher, cipherOffset)
    }

    @Throws(IllegalArgumentException::class, ArrayIndexOutOfBoundsException::class)
    fun decryptFinal(cipher: ByteArray, cipherOffset: Int, cipherLen: Int, plain: ByteArray, plainOffset: Int): Int {
        return this.decrypt(cipher, cipherOffset, cipherLen, plain, plainOffset)
    }

    @Throws(IllegalArgumentException::class)
    fun init(decrypting: Boolean, key: ByteArray?, iv: ByteArray?) {
        if ((key != null && iv != null) && iv.size == this.blockSize) {
            this.iv = iv
            this.reset()
            embeddedCipher.init(decrypting, key)
        } else {
            throw IllegalArgumentException("Internal error")
        }
    }

    fun reset() {
        System.arraycopy(this.iv, 0, this.r, 0, this.blockSize)
    }

    fun save() {
        if (this.rSave == null) {
            this.rSave = ByteArray(this.blockSize)
        }

        System.arraycopy(this.r, 0, this.rSave, 0, this.blockSize)
    }

    fun restore() {
        System.arraycopy(this.rSave, 0, this.r, 0, this.blockSize)
    }

    fun encrypt(plain: ByteArray, plainOffset: Int, plainLen: Int, cipher: ByteArray, cipherOffset: Int): Int {
        var retLen: Int
        if (plainLen <= 0) {
            retLen = plainLen
        } else {
            blockSizeCheck(plainLen, this.blockSize)
            retLen = implEncrypt(plain, plainOffset, plainLen, cipher, cipherOffset)
        }
        return retLen
    }

    private fun implEncrypt(
        plain: ByteArray,
        plainOffsetIn: Int,
        plainLen: Int,
        cipher: ByteArray,
        cipherOffsetIn: Int
    ): Int {
        var plainOffset = plainOffsetIn
        var cipherOffset = cipherOffsetIn
        val endIndex = plainOffset + plainLen
        while (plainOffset < endIndex) {
            for (i in 0 until blockSize) {
                k.set(i, (plain[i + plainOffset].toInt() xor r[i].toInt()).toByte())
            }

            embeddedCipher.encryptBlock(k, 0, cipher, cipherOffset)
            System.arraycopy(cipher, cipherOffset, r, 0, blockSize)
            plainOffset += blockSize
            cipherOffset += blockSize
        }

        return plainLen
    }


    fun decrypt(cipher: ByteArray, cipherOffset: Int, cipherLen: Int, plain: ByteArray, plainOffset: Int): Int {
        if (cipherLen <= 0) {
            return cipherLen
        } else {
            blockSizeCheck(cipherLen, blockSize)
            return implDecrypt(cipher, cipherOffset, cipherLen, plain, plainOffset)
        }
    }


    private fun implDecrypt(
        cipher: ByteArray,
        cipherOffsetIn: Int,
        cipherLen: Int,
        plain: ByteArray,
        plainOffsetIn: Int
    ): Int {
        var cipherOffset = cipherOffsetIn
        var plainOffset = plainOffsetIn
        val endIndex = cipherOffset + cipherLen
        while (cipherOffset < endIndex) {
            embeddedCipher.decryptBlock(cipher, cipherOffset, this.k, 0)

            for (i in 0 until this.blockSize) {
                plain.set(i + plainOffset, (k[i].toInt() xor r[i].toInt()).toByte())
            }

            System.arraycopy(cipher, cipherOffset, this.r, 0, this.blockSize)
            cipherOffset += this.blockSize
            plainOffset += this.blockSize
        }

        return cipherLen
    }

    companion object {
        fun blockSizeCheck(len: Int, blockSize: Int) {
            if ((len % blockSize) != 0) {
                throw ArrayIndexOutOfBoundsException("Internal error in input buffering")
            }
        }
    }
}
