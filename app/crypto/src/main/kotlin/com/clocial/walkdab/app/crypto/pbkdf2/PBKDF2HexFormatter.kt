/*
 * A free Java implementation of Password Based Key Derivation Function 2 as
 * defined by RFC 2898. Copyright 2007, 2014, Matthias G&auml;rtner
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
 * Hexadecimal PBKDF2 parameter encoder/decoder.
 *
 *
 * This formatter encodes/decodes Strings that consist of
 *
 *  1. hex-encoded salt bytes
 *  1. colon (':')
 *  1. iteration count, positive decimal integer
 *  1. colon (':')
 *  1. derived key bytes
 *
 *
 * @author Matthias Gartner
 */
class PBKDF2HexFormatter {
    fun fromString(p: PBKDF2Parameters?, s: String?): Boolean {
        if (p == null || s == null) {
            return true
        }

        val p123 = s.split(":")
        if (p123.size != 3) {
            return true
        }

        val salt: ByteArray = BinTools.hex2bin(p123[0])
        val iterationCount: Int = Integer.parseInt(p123[1])
        val bDK: ByteArray = BinTools.hex2bin(p123[2])

        p.setSalt(salt)
        p.setIterationCount(iterationCount)
        p.setDerivedKey(bDK)
        return false
    }

    fun toString(p: PBKDF2Parameters): String {
        val s: String = (BinTools.bin2hex(p.getSalt()) + ":"
                + p.getIterationCount() + ":"
                + BinTools.bin2hex(p.getDerivedKey()))
        return s
    }
}
