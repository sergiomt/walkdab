/*
 * Free auxiliary functions. Copyright 2007, 2014, Matthias G&auml;rtner
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.clocial.walkdab.app.crypto.pbkdf2

/**
 * Free auxiliary functions
 *
 * @author Matthias Grtner
 */
object BinTools {

    val hex: String = "0123456789ABCDEF"

    /**
     * Simple binary-to-hexadecimal conversion.
     *
     * @param b
     * Input bytes. May be `null`.
     * @return Hexadecimal representation of b. Uppercase A-F, two characters
     * per byte. Empty string on `null` input.
     */
    fun bin2hex(b: ByteArray?): String {
        if (b == null) {
            return ""
        }
        val sb = StringBuffer(2 * b.size)
        for (i in b.indices) {
            val v = (256 + b[i]) % 256
            sb.append(hex[(v / 16) and 15])
            sb.append(hex[(v % 16) and 15])
        }
        return sb.toString()
    }

    /**
     * Convert hex string to array of bytes.
     *
     * @param s
     * String containing hexadecimal digits. May be `null`.
     * On odd length leading zero will be assumed.
     * @return Array on bytes, non-`null`.
     * @throws IllegalArgumentException
     * when string contains non-hex character
     */
    fun hex2bin(s: String?): ByteArray {
        var m: String
        if (s == null) {
            // Allow empty input string.
            m = ""
        } else if (s.length % 2 != 0) {
            // Assume leading zero for odd string length
            m = "0$s"
        } else {
            m = s
        }
        val r = ByteArray(m.length / 2)
        var i = 0
        var n = 0
        while (i < m.length) {
            val h: Char = m[i++]
            val l: Char = m[i++]
            r[n] = (hex2bin(h) * 16 + hex2bin(l)).toByte()
            n++
        }
        return r
    }

    /**
     * Convert hex digit to numerical value.
     *
     * @param c
     * 0-9, a-f, A-F allowd.
     * @return 0-15
     * @throws IllegalArgumentException
     * on non-hex character
     */
    fun hex2bin(c: Char): Int {
        if (c in '0'..'9') {
            return (c.code - '0'.code)
        }
        if (c in 'A'..'F') {
            return (c.code - 'A'.code + 10)
        }
        if (c in 'a'..'f') {
            return (c.code - 'a'.code + 10)
        }
        throw IllegalArgumentException("Input string may only contain hex digits, but found '$c'")
    }

}
