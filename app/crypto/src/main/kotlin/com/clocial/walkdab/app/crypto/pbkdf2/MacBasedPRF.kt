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
 * Default PRF implementation based on standard javax.crypt.Mac mechanisms.
 *
 * @author Matthias Grtner
 */
class MacBasedPRF() : PRF {

    protected var macSpiSHA2: HmacSHA2 = HmacSHA2()

    protected var hLengt: Int = macSpiSHA2.engineGetMacLength()

    override fun doFinal(input: ByteArray): ByteArray {
        macSpiSHA2.engineUpdate(input, 0, input.size)
        val mac: ByteArray = macSpiSHA2.engineDoFinal()
        macSpiSHA2.engineReset()
        return mac
    }

    override fun getHLen(): Int {
        return hLengt
    }

    override fun init(secret: ByteArray) {
        macSpiSHA2.engineInit(secret)
    }
}
