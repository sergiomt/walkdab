package com.clocial.walkdab.app.crypto.pbkdf2

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

/**
 * This **Password Based Key Derivation Function 2** implementation.
 * Request for Comments: 2898 PKCS #5: Password-Based Cryptography Specification
 *
 * PBKDF2 (P, S, c, dkLen)
 *
 * Options:
 *
 *  * PRF underlying pseudorandom function (hLen denotes the length in octets
 * of the pseudorandom function output). PRF is pluggable.
 *
 * Input:
 *
 *  * P password, an octet string
 *  * S salt, an octet string
 *  * c iteration count, a positive integer
 *  * dkLen intended length in octets of the derived key, a positive integer,
 * at most (2^32 - 1) * hLen
 *
 *
 * Output:
 *
 *  * DK derived key, a dkLen-octet string
 *
 * @see [RFC 2898](http://tools.ietf.org/html/rfc2898)
 *
 * @author Matthias Gartner
 * @version 2.0
 */
open class PBKDF2Base : PBKDF2 {

    protected var pbkdf2params: PBKDF2Parameters

    protected var prf: PRF

    /**
     * Constructor for PBKDF2 implementation object. PBKDF2 parameters are
     * passed so that this implementation knows iteration count, method to use
     * and String encoding.
     *
     * @param parameters
     * Data holder for iteration count, method to use etc.
     */
    constructor(params: PBKDF2Parameters) {
        pbkdf2params = params
        prf = MacBasedPRF()
    }

    override fun deriveKey(inputPassword: String?): ByteArray {
        return deriveKey(inputPassword, 0)
    }

    override fun deriveKey(inputPassword: String?, dkLen: Int): ByteArray {
        var inputPsswrd = inputPassword ?: ""
        var psswrdLen: Int
        val chrSet: String? = pbkdf2params.getHashCharset()
        val p = inputPsswrd.toByteArray(
            if (chrSet == null) {Charsets.UTF_8} else {charset(chrSet)}
        )
        prf.init(p)
        if (dkLen == 0) {
            psswrdLen = prf.getHLen()
        } else {
            psswrdLen = dkLen
        }
        val r = PBKDF2(prf, pbkdf2params.getSalt(), pbkdf2params.getIterationCount(), psswrdLen)
        return r
    }

    override fun verifyKey(inputPassword: String): Boolean {
        val referenceKey: ByteArray = getParameters().getDerivedKey()
        if (referenceKey == null || referenceKey.isEmpty()) {
            return false
        }
        val inputKey = deriveKey(inputPassword, referenceKey.size)

        if (inputKey == null || inputKey.size != referenceKey.size) {
            return false
        }
        var z = 0
        for (i in inputKey.indices) {
            z = z or (inputKey[i].toInt() xor referenceKey[i].toInt())
        }
        return (z == 0)
    }


    override fun getPseudoRandomFunction(): PRF {
        return prf
    }

    /**
     * Core Password Based Key Derivation Function 2.
     *
     * @see [RFC 2898 5.2](http://tools.ietf.org/html/rfc2898)
     *
     * @param prf
     * Pseudo Random Function (i.e. HmacSHA1)
     * @param S
     * Salt as array of bytes. `null` means no salt.
     * @param c
     * Iteration count (see RFC 2898 4.2)
     * @param dkLen
     * desired length of derived key.
     * @return internal byte array
     */
    private fun PBKDF2(prf: PRF, S: ByteArray?, c: Int, dkLen: Int): ByteArray {
        var s: ByteArray
        if (S == null) {
            s = ByteArray(0)
        } else {
            s = S
        }
        val hLen: Int = prf.getHLen()
        val l = ceil(dkLen, hLen)
        val r = dkLen - (l - 1) * hLen
        val t = ByteArray(l * hLen)
        var tiOffset = 0
        for (i in 1..l) {
            _F(t, tiOffset, prf, s, c, i)
            tiOffset += hLen
        }
        if (r < hLen) {
            // Incomplete last block
            val dk = ByteArray(dkLen)
            System.arraycopy(t, 0, dk, 0, dkLen)
            return dk
        }
        return t
    }

    /**
     * Integer division with ceiling function.
     *
     * @see [RFC 2898 5.2 Step 2.](http://tools.ietf.org/html/rfc2898)
     *
     * @param a Numerator
     * @param b Denominator
     * @return ceil(a/b)
     */
    protected fun ceil(a: Int, b: Int): Int {
        var m = 0
        if (a % b > 0) {
            m = 1
        }
        return a / b + m
    }

    /**
     * Function F.
     *
     * @see [RFC 2898 5.2 Step 3.](http://tools.ietf.org/html/rfc2898)
     *
     * @param dest
     * Destination byte buffer
     * @param offset
     * Offset into destination byte buffer
     * @param prf
     * Pseudo Random Function
     * @param S
     * Salt as array of bytes
     * @param c
     * Iteration count
     * @param blockIndex
     * The block index (&gt;= 1).
     */
    protected fun _F(
        dest: ByteArray, offset: Int, prf: PRF, S: ByteArray, c: Int, blockIndex: Int) {
        val hLen: Int = prf.getHLen()
        val U_r = ByteArray(hLen)

        // U0 = S || INT (i);
        var U_i = ByteArray(S.size + 4)
        System.arraycopy(S, 0, U_i, 0, S.size)
        INT(U_i, S.size, blockIndex)

        for (i in 0 until c) {
            U_i = prf.doFinal(U_i)
            xor(U_r, U_i)
        }
        System.arraycopy(U_r, 0, dest, offset, hLen)
    }

    /**
     * Block-Xor. Xor source bytes into destination byte buffer. Destination
     * buffer must be same length or less than source buffer.
     *
     * @param dest destination byte buffer
     * @param src source bytes
     */
    private fun xor(dest: ByteArray, src: ByteArray) {
        for (i in dest.indices) {
            dest.set(i, (dest[i].toInt() xor src[i].toInt()).toByte())
        }
    }

    /**
     * Four-octet encoding of the integer i, most significant octet first.
     *
     * @see [RFC 2898 5.2 Step 3.](http://tools.ietf.org/html/rfc2898)
     *
     * @param dest destination byte buffer
     * @param offset zero-based offset into dest
     * @param i the integer to encode
     */
    private fun INT(dest: ByteArray, offset: Int, i: Int) {
        dest[offset + 0] = (i / (256 * 256 * 256)).toByte()
        dest[offset + 1] = (i / (256 * 256)).toByte()
        dest[offset + 2] = (i / (256)).toByte()
        dest[offset + 3] = i.toByte()
    }

    override fun getParameters(): PBKDF2Parameters {
        return pbkdf2params
    }

    fun setParameters(params: PBKDF2Parameters)  {
        pbkdf2params = params
    }
}

