/*
 * Copyright (c) 2015, Bubelich Mykola (bubelich.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.clocial.walkdab.app.encoding

/**
 * Author: Bubelich Mykola
 * Date: 2015-06-01
 *
 * Implementation of jBaseZ85 data encoding/decoding
 *
 * @author Bubelich Mykola (bubelich.com)
 * @link https://github.com/thesimj/jBaseZ85 (github)
 */
object BaseZ85 {

    private val _ALPHA =
        "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-:+=^!/*?&<>()[]{}@%$#".toCharArray()

    private val _RALPHA = intArrayOf(
        68, 0, 84, 83, 82, 72, 0, 75, 76, 70, 65, 0, 63, 62,
        69, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 64, 0, 73, 66, 74, 71,
        81, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61,
        77, 0, 78, 67, 0, 0, 10, 11, 12, 13, 14, 15, 16, 17, 18,
        19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,
        33, 34, 35, 79, 0, 80
    )

    private const val _RALSHIFT = 33

    /**
     * Encode the Byte array into jBaseZ85 format.
     *
     * @param input byte[] Array of byte to encode.
     * @return String The encoded String
     *
     * @throws RuntimeException
     */
    @Throws(RuntimeException::class)
    fun encode(input: ByteArray): String {

        var length = input.size
        var index = 0
        var buff = ByteArray(4)

        // Use mutable StringBuilder for fast string append //
        val sb = StringBuilder(input.size * 5 / 4 + 1)
        while (length >= 4) {

            // copy input to buff //
            buff[3] = input[index++]
            buff[2] = input[index++]
            buff[1] = input[index++]
            buff[0] = input[index++]

            // Append result string to StringBuilder
            sb.append(encodeQuarter(buff))
            length -= 4
        }

        // Padding zone //
        if (length > 0) {
            buff = ByteArray(length)
            for (i in length - 1 downTo 0) buff[i] = input[index++]

            // Append result string to StringBuilder
            sb.append(encodePadding(buff))
        }

        // Return whole string //
        return sb.toString()
    }

    /**
     * Decodes a jBaseZ85 encoded string.
     *
     * @param input byte[] String The encoded jBaseZ85 String.
     * @return byte[] The decoded array of bytes.
     *
     * @throws RuntimeException
     */
    @Throws(RuntimeException::class)
    fun decode(input: String): ByteArray {

        var length = input.length
        var index = 0
        val buff = CharArray(5)
        var bytebuff = ByteArray(length * 4 / 5)
        var currentPosition = 0
        var decodedBytes: ByteArray
        while (length >= 5) {
            buff[0] = input[index++]
            buff[1] = input[index++]
            buff[2] = input[index++]
            buff[3] = input[index++]
            buff[4] = input[index++]
            decodedBytes = decodeQuarter(buff)
            bytebuff = safePut(bytebuff, currentPosition, decodedBytes)
            currentPosition += decodedBytes.size
            length -= 5
        }

        // If last length > 0 Then need padding //
        if (length > 0) {

            // create padding buffer //
            val padding = CharArray(length)

            // copy last input value to padding buffer //
            for (i in 0 until length) padding[i] = input[index++]

            // decode padding //
            decodedBytes = decodePadding(padding)
            bytebuff = safePut(bytebuff, currentPosition, decodedBytes)
            currentPosition += decodedBytes.size
        }

        if (currentPosition == 0) throw RuntimeException("Output is empty!")
        return bytebuff.copyOf(currentPosition)
    }

    private fun safePut(buffer: ByteArray, index: Int, bytes: ByteArray): ByteArray {
        val extBuffer = if (bytes.size + index < buffer.size) buffer else reallocate(buffer, index, bytes.size)
        bytes.copyInto(extBuffer, index, 0, bytes.size)
        return extBuffer
    }

    private fun reallocate(buffer: ByteArray, index: Int, minExtension: Int): ByteArray {
        val extLength = if (minExtension > 32767) minExtension else 32767
        val extBuffer = ByteArray(buffer.size + extLength)
        buffer.copyInto(extBuffer, 0, 0, index)
        return extBuffer
    }

    private fun encodeQuarter(data: ByteArray): CharArray {
        val value: Long = data[0].toLong() and 0x00000000000000FFL or
                (data[1].toLong() and 0x00000000000000FFL shl 8) or
                (data[2].toLong() and 0x00000000000000FFL shl 16) or
                (data[3].toLong() and 0x00000000000000FFL shl 24)
        val out = CharArray(5)
        out[0] = _ALPHA[(value / 0x31C84B1L % 85).toInt()]
        out[1] = _ALPHA[(value / 0x95EEDL % 85).toInt()]
        out[2] = _ALPHA[(value / 0x1C39L % 85).toInt()]
        out[3] = _ALPHA[(value / 0x55L % 85).toInt()]
        out[4] = _ALPHA[(value % 85).toInt()]
        return out
    }

    /**
     * Encode padding scheme
     *
     * @param data byte[] Array of length = 4 of data
     * @return char[] Encoded padding
     */
    private fun encodePadding(data: ByteArray): CharArray {
        var value: Long = 0
        val length = data.size * 5 / 4 + 1
        val out = CharArray(length)
        when (data.size) {
            3 -> {
                value = value or (data[2].toLong() and 0x00000000000000FFL shl 16)
                value = value or (data[1].toLong() and 0x00000000000000FFL shl 8)
            }
            2 -> value = value or (data[1].toLong() and 0x00000000000000FFL shl 8)
        }
        value = value or (data[0].toLong() and 0x00000000000000FFL)
        when (data.size) {
            3 -> {
                out[3] = _ALPHA[(value / 0x95EEDL % 85).toInt()]
                out[2] = _ALPHA[(value / 0x1C39L % 85).toInt()]
            }
            2 -> out[2] = _ALPHA[(value / 0x1C39L % 85).toInt()]
        }
        out[1] = _ALPHA[(value / 0x55L % 85).toInt()]
        out[0] = _ALPHA[(value % 85).toInt()]
        return out
    }

    private fun decodeQuarter(data: CharArray): ByteArray {
        var value: Long = 0
        value += _RALPHA[(data[0] - _RALSHIFT).code] * 0x31C84B1L
        value += _RALPHA[(data[1] - _RALSHIFT).code] * 0x95EEDL
        value += _RALPHA[(data[2] - _RALSHIFT).code] * 0x1C39L
        value += _RALPHA[(data[3] - _RALSHIFT).code] * 0x55L
        value += _RALPHA[(data[4] - _RALSHIFT).code]
        return byteArrayOf(
            (value ushr 24).toByte(),
            (value ushr 16).toByte(),
            (value ushr 8).toByte(),
            value.toByte()
        )
    }

    private fun decodePadding(data: CharArray): ByteArray {
        var value: Long = 0
        val length = data.size * 4 / 5
        when (data.size) {
            4 -> {
                value += _RALPHA[(data[3] - _RALSHIFT).code] * 0x95EEDL
                value += _RALPHA[(data[2] - _RALSHIFT).code] * 0x1C39L
                value += _RALPHA[(data[1] - _RALSHIFT).code] * 0x55L
            }
            3 -> {
                value += _RALPHA[(data[2] - _RALSHIFT).code] * 0x1C39L
                value += _RALPHA[(data[1] - _RALSHIFT).code] * 0x55L
            }
            2 -> value += _RALPHA[(data[1] - _RALSHIFT).code] * 0x55L
        }
        value += _RALPHA[(data[0] - _RALSHIFT).code]
        val buff = ByteArray(length)
        for (i in length - 1 downTo 0) {
            buff[length - i - 1] = (value ushr 8 * i).toByte()
        }
        return buff
    }
}