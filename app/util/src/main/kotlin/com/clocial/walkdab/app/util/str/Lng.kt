package com.clocial.walkdab.app.util.str

import kotlin.math.max

object Lng {

    fun toHexString(i: Long): String {
        return toUnsignedString(i, 4)
    }

    fun toHexString(i: Int): String {
        return toUnsignedString(i, 4)
    }

    fun numberOfLeadingZeros(i: Long): Int {
        val x: Int = (i ushr 32).toInt()
        return if (x == 0) 32 + Integer.numberOfLeadingZeros(i.toInt())
        else Integer.numberOfLeadingZeros(x)
    }

    fun numberOfLeadingZeros(i: Int): Int {
        var i = i
        if (i <= 0) return if (i == 0) 32 else 0
        var n = 31
        if (i >= 1 shl 16) {
            n -= 16
            i = i ushr 16
        }
        if (i >= 1 shl 8) {
            n -= 8
            i = i ushr 8
        }
        if (i >= 1 shl 4) {
            n -= 4
            i = i ushr 4
        }
        if (i >= 1 shl 2) {
            n -= 2
            i = i ushr 2
        }
        return n - (i ushr 1)
    }

    private fun toUnsignedString(intValue: Int, shift: Int): String {
        // assert shift > 0 && shift <=5 : "Illegal shift value";
        val mag = 32 - numberOfLeadingZeros(intValue)
        val chars = max(((mag + (shift - 1)) / shift).toDouble(), 1.0).toInt()
        val buf = ByteArray(chars)
        formatUnsignedInt(intValue, shift, buf, chars)
        return String(buf, Charsets.ISO_8859_1)
    }

    private fun toUnsignedString(longValue: Long, shift: Int): String {
        // assert shift > 0 && shift <=5 : "Illegal shift value";
        val mag = 64 - numberOfLeadingZeros(longValue)
        val chars = max(((mag + (shift - 1)) / shift).toDouble(), 1.0).toInt()
        val buf = ByteArray(chars)
        formatUnsignedLong(longValue, shift, buf, 0, chars)
        return String(buf, Charsets.ISO_8859_1)
    }

    private fun formatUnsignedInt(intValue: Int, shift: Int, buf: ByteArray, len: Int) {
        var i = intValue
        var charPos = len
        val radix = 1 shl shift
        val mask = radix - 1
        do {
            buf[--charPos] = digits[i and mask].code.toByte()
            i = i ushr shift
        } while (charPos > 0)
    }

    private fun formatUnsignedLong(longValue: Long, shift: Int, buf: ByteArray, offset: Int, len: Int) {
        var l = longValue
        var charPos = offset + len
        val radix = 1 shl shift
        val mask = radix - 1
        do {
            buf[--charPos] = digits[l.toInt() and mask].code.toByte()
            l = l ushr shift
        } while (charPos > offset)
    }

    private val digits: CharArray = charArrayOf(
        '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z'
    )

}