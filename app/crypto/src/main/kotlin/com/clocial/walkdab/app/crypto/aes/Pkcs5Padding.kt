package com.clocial.walkdab.app.crypto.aes

import kotlin.Throws

internal class Pkcs5Padding(private val blockSize: Int) {
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun padWithLen(`in`: ByteArray?, off: Int, len: Int) {
        if (`in` != null) {
            val idx: Int = Math.addExact(off, len)
            if (idx > `in`.size) {
                throw ArrayIndexOutOfBoundsException("The buffer is not big enough to hold padding")
            } else {
                val paddingOctet = (len and 255).toByte()
                for (i in off until idx) `in`[i] = paddingOctet
            }
        }
    }

    fun unpad(`in`: ByteArray?, off: Int, len: Int): Int {
        if (`in` != null && len != 0) {
            val idx: Int = Math.addExact(off, len)
            val lastByte = `in`[idx - 1]
            val padValue = lastByte.toInt() and 255
            if (padValue >= 1 && padValue <= blockSize) {
                val start = idx - padValue
                if (start < off) {
                    return -1
                } else {
                    for (i in start until idx) {
                        if (`in`[i] != lastByte) {
                            return -1
                        }
                    }

                    return start
                }
            } else {
                return -1
            }
        } else {
            return 0
        }
    }

    fun padLength(len: Int): Int {
        val paddingOctet = this.blockSize - len % this.blockSize
        return paddingOctet
    }
}

