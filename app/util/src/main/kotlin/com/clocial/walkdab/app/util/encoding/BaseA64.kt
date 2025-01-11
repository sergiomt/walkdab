/*
 * Base64Encoder.java February 2014
 *
 * Copyright (C) 2014, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.clocial.walkdab.app.encoding

/**
 * The `Base64Encoder` is used to encode and decode base64
 * content. The implementation used here provides a reasonably fast
 * memory efficient encoder for use with input and output streams. It
 * is possible to achieve higher performance, however, ease of use
 * and convenience are the priorities with this implementation. This
 * can only decode complete blocks.
 *
 * @author Niall Gallagher
 */
object BaseA64 {
    /**
     * This maintains reference data used to fast decoding.
     */
    private val REFERENCE = intArrayOf(
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 62, 0, 0, 0, 63,
        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 0, 0, 0, 0, 0, 0,
        0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 0, 0, 0, 0, 0,
        0, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 0, 0, 0, 0, 0
    )

    /**
     * This contains the base64 alphabet used for encoding.
     */
    private val ALPHABET = charArrayOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    )
    /**
     * This method is used to encode the specified byte array of binary
     * data in to base64 data. The block is complete and must be decoded
     * as a complete block.
     *
     * @param buf this is the binary data to be encoded
     * @param off this is the offset to read the binary data from
     * @param len this is the length of data to encode from the array
     *
     * @return this is the base64 encoded value of the data
     */
    /**
     * This method is used to encode the specified byte array of binary
     * data in to base64 data. The block is complete and must be decoded
     * as a complete block.
     *
     * @param buf this is the binary data to be encoded
     *
     * @return this is the base64 encoded value of the data
     */
    @JvmOverloads
    fun encode(buf: ByteArray, off: Int = 0, len: Int = buf.size): CharArray {
        val text = CharArray((len + 2) / 3 * 4)
        val last = off + len
        var a = 0
        var i = 0
        while (i < last) {
            val one = buf[i++].toInt()
            val two = if (i < len) buf[i++].toInt() else 0
            val three = if (i < len) buf[i++].toInt() else 0
            val mask = 0x3F
            text[a++] = ALPHABET[one shr 2 and mask]
            text[a++] = ALPHABET[one shl 4 or (two and 0xFF shr 4) and mask]
            text[a++] = ALPHABET[two shl 2 or (three and 0xFF shr 6) and mask]
            text[a++] = ALPHABET[three and mask]
        }
        when (len % 3) {
            1 -> {
                text[--a] = '='
                text[--a] = '='
            }
            2 -> text[--a] = '='
        }
        return text
    }
    /**
     * This is used to decode the provide base64 data back in to an
     * array of binary data. The data provided here must be a full block
     * of base 64 data in order to be decoded.
     *
     * @param text this is the base64 text to be decoded
     * @param off this is the offset to read the text data from
     * @param len this is the length of data to decode from the text
     *
     * @return this returns the resulting byte array
     */
    /**
     * This is used to decode the provide base64 data back in to an
     * array of binary data. The data provided here must be a full block
     * of base 64 data in order to be decoded.
     *
     * @param text this is the base64 text to be decoded
     *
     * @return this returns the resulting byte array
     */
    fun decode(text: CharArray, off: Int = 0, len: Int = text.size): ByteArray {
        var delta = 0
        if (text[off + len - 1] == '=') {
            delta = if (text[off + len - 2] == '=') 2 else 1
        }
        val buf = ByteArray(len * 3 / 4 - delta)
        val mask = 0xff
        var index = 0
        var i = 0
        while (i < len) {
            val pos = off + i
            val one = REFERENCE[text[pos].code]
            val two = REFERENCE[text[pos + 1].code]
            buf[index++] = (one shl 2 or (two shr 4) and mask).toByte()
            if (index >= buf.size) {
                return buf
            }
            val three = REFERENCE[text[pos + 2].code]
            buf[index++] = (two shl 4 or (three shr 2) and mask).toByte()
            if (index >= buf.size) {
                return buf
            }
            val four = REFERENCE[text[pos + 3].code]
            buf[index++] = (three shl 6 or four and mask).toByte()
            i += 4
        }
        return buf
    }
}