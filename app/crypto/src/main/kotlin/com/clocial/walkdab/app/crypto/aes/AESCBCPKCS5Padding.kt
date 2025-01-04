package com.clocial.walkdab.app.crypto.aes

import kotlin.collections.copyOf
import kotlin.random.Random

class AESCBCPKCS5Padding(encryptionKey: ByteArray) {

    private val key: ByteArray = encryptionKey.clone()

    private val blockSize = 16

    private val unitBytes = blockSize

    private val padding: Pkcs5Padding = Pkcs5Padding(blockSize)

    private var diffBlocksize = blockSize

    private var buffered = 0

    private val buffer: ByteArray = ByteArray(blockSize * 2)

    @Throws(IllegalArgumentException::class)
    fun init(cipher: CBCBlockChain, decrypting: Boolean, iv: ByteArray?, customRandom: Random?) {
        var random: Random = customRandom ?: Random.Default
        var ivBytes: ByteArray? = null

        try {
            if (iv != null) {
                ivBytes = iv.clone()
                if (ivBytes.size != this.blockSize) {
                    throw IllegalArgumentException("Wrong IV length: must be " + this.blockSize + " bytes long")
                }
            }

            if (ivBytes == null) {
                if (decrypting) {
                    throw IllegalArgumentException("Parameters missing")
                }

                ivBytes = ByteArray(this.blockSize)
                random.nextBytes(ivBytes)
            }

            this.buffered = 0
            this.diffBlocksize = this.blockSize
            cipher.init(decrypting, key, ivBytes)
        } finally {
            key.fill(0)
        }
    }

    @Throws(IllegalArgumentException::class)
    fun encrypt(input: ByteArray): ByteArray {
        return enDecrypt(input, 0, input.size, false)
    }

    @Throws(IllegalArgumentException::class)
    fun decrypt(input: ByteArray): ByteArray {
        return enDecrypt(input, 0, input.size, true)
    }

    @Throws(IllegalArgumentException::class, ArrayIndexOutOfBoundsException::class)
    private fun enDecrypt(input: ByteArray, inputOffset: Int, inputLen: Int, decrypting: Boolean): ByteArray {
        val output = ByteArray(getOutputSizeByOperation(inputLen, decrypting))
        val finalBuf = prepareInputBuffer(input, inputOffset, inputLen, output, 0, decrypting)
        val finalOffset = if (finalBuf == input) inputOffset else 0
        val finalBufLen = if (finalBuf == input) inputLen else finalBuf.size
        val outLen = fillOutputBuffer(finalBuf, finalOffset, output, 0, finalBufLen, input, decrypting)
        endDoFinal()
        if (outLen < output.size) {

            val copy: ByteArray = output.copyOf(outLen)
            if (decrypting) {
                output.fill(0)
            }

            return copy
        } else {
            return output
        }
    }

    private fun endDoFinal() {
        this.buffered = 0
        this.diffBlocksize = this.blockSize
    }

    private fun getOutputSizeByOperation(inputLen: Int, decrypting: Boolean): Int {
        var totalLen = this.buffered
        totalLen = Math.addExact(totalLen, inputLen)
        if (!decrypting) {
            if (this.unitBytes != this.blockSize) {
                if (totalLen < this.diffBlocksize) {
                    totalLen = this.diffBlocksize
                } else {
                    val residue = (totalLen - this.diffBlocksize) % this.blockSize
                    totalLen = Math.addExact(totalLen, this.blockSize - residue)
                }
            } else {
                totalLen = Math.addExact(totalLen, padding.padLength(totalLen))
            }
        }

        return totalLen
    }

    @Throws(IllegalArgumentException::class, ArrayIndexOutOfBoundsException::class)
    private fun prepareInputBuffer(
        input: ByteArray,
        inputOffset: Int,
        inputLen: Int,
        output: ByteArray,
        outputOffset: Int,
        decrypting: Boolean
    ): ByteArray {
        val len: Int = Math.addExact(this.buffered, inputLen)
        var paddingLen = 0
        if (this.unitBytes != this.blockSize) {
            paddingLen = if (len < this.diffBlocksize) {
                diffBlocksize - len
            } else {
                blockSize - (len - this.diffBlocksize) % this.blockSize
            }
        } else {
            paddingLen = padding.padLength(len)
        }

        if (decrypting && paddingLen > 0 && paddingLen != this.blockSize) {
            throw IllegalArgumentException(
                String.format(
                    "Input length must be multiple of %d when decrypting with PKCS5Padding",
                    this.blockSize
                )
            )
        } else if (this.buffered == 0 && decrypting && (input != output || outputOffset - inputOffset >= inputLen || inputOffset - outputOffset >= buffer.size)) {
            return input
        } else {
            if (decrypting) {
                paddingLen = 0
            }

            val finalBuf = ByteArray(Math.addExact(len, paddingLen))
            if (this.buffered != 0) {
                System.arraycopy(buffer, 0, finalBuf, 0, buffered)
                if (!decrypting) {
                    buffer.fill(0)
                }
            }

            if (inputLen != 0) {
                System.arraycopy(input, inputOffset, finalBuf, buffered, inputLen)
            }

            if (paddingLen != 0) {
                padding.padWithLen(finalBuf, Math.addExact(buffered, inputLen), paddingLen)
            }

            return finalBuf
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun finalNoPadding(
        input: ByteArray,
        inOfs: Int,
        output: ByteArray,
        outOfs: Int,
        len: Int,
        decrypting: Boolean
    ): Int {
        if (len != 0) {
            val cipher: CBCBlockChain = CBCBlockChain(AesCryptImpl(decrypting, key))
            init(cipher, decrypting, initialization_vector, null) // TO-DO random != null, iv != null
            if (len % unitBytes != 0) {
                throw IllegalArgumentException("Input length (with padding) not multiple of $unitBytes bytes")
            } else {
                val outLen: Int = if (decrypting) {
                    cipher.decryptFinal(input, inOfs, len, output, outOfs)
                } else {
                    cipher.encryptFinal(input, inOfs, len, output, outOfs)
                }

                return outLen
            }
        } else {
            return 0
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun unpad(outLen: Int, off: Int, outWithPadding: ByteArray?): Int {
        val padStart: Int = padding.unpad(outWithPadding, off, outLen)
        if (padStart < 0) {
            throw IllegalArgumentException("Given final block not properly padded. Such issues can arise if a bad key is used during decryption.")
        } else {
            return padStart - off
        }
    }

    @Throws(ArrayIndexOutOfBoundsException::class, IllegalArgumentException::class)
    private fun fillOutputBuffer(
        finalBuf: ByteArray,
        finalOffset: Int,
        output: ByteArray,
        outOfs: Int,
        finalBufLen: Int,
        input: ByteArray,
        decrypting: Boolean
    ): Int {
        val l: Int
        try {
            var len = finalNoPadding(finalBuf, finalOffset, output, outOfs, finalBufLen, decrypting)
            if (decrypting) {
                len = unpad(len, outOfs, output)
            }

            l = len
        } finally {
            if (!decrypting && finalBuf != input) {
                finalBuf.fill(0)
            }
        }
        return l
    }

    companion object {
        private val initialization_vector: ByteArray =
            byteArrayOf(54, 34, 7, 3, 23, 78, 31, 68, 32, 40, 96, 43, 23, 54, 23, 76)
    }
}
