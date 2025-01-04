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
 * Parameter data holder for PBKDF2 configuration.
 *
 * @author Matthias Grtner
 */
class PBKDF2Parameters {

    protected var saltBytes: ByteArray?

    protected var iterCount: Int

    protected var hashAlgorithmStr: String

    protected var hashCharsetStr: String

    /**
     * The derived key is actually only a convenience to store a reference
     * derived key. It is not used during computation.
     */
    protected var derivedKeyBytes: ByteArray

    /**
     * Constructor. SHA256 with ISO-8859-1 as hash character set and 2000 for iteration count.
     *
     */
    constructor() {
        hashAlgorithmStr = "SHA256"
        hashCharsetStr = "ISO-8859-1"
        saltBytes = byteArrayOf(121, 119, 37, 42, 58, 61, 77, 84) // SecureRandom.getInstance("SHA1PRNG").nextBytes(salt)
        iterCount = 2000
        derivedKeyBytes = byteArrayOf()
    }

    /**
     * Constructor.
     *
     * @param hashAlgorithm
     * for example HMacSHA1 or HMacMD5
     * @param hashCharset
     * for example UTF-8
     * @param salt
     * Salt as byte array, may be `null` (not
     * recommended)
     * @param iterationCount
     * Number of iterations to execute. Recommended value 1000.
     */
    constructor(
        hashAlgorithm: String, hashCharset: String,
        salt: ByteArray?, iterationCount: Int
    ) {
        this.hashAlgorithmStr = hashAlgorithm
        this.hashCharsetStr = hashCharset
        this.saltBytes = salt
        this.iterCount = iterationCount
        this.derivedKeyBytes = byteArrayOf()
    }

    fun getIterationCount(): Int {
        return iterCount
    }

    fun setIterationCount(iterationCount: Int) {
        this.iterCount = iterationCount
    }

    fun getSalt(): ByteArray? {
        return saltBytes
    }

    fun setSalt(salt: ByteArray?) {
        this.saltBytes = salt
    }

    fun getDerivedKey(): ByteArray {
        return derivedKeyBytes
    }

    fun setDerivedKey(derivedKey: ByteArray) {
        this.derivedKeyBytes = derivedKey
    }

    fun getHashAlgorithm(): String {
        return hashAlgorithmStr
    }

    fun setHashAlgorithm(hashAlgorithm: String) {
        this.hashAlgorithmStr = hashAlgorithm
    }

    fun getHashCharset(): String {
        return hashCharsetStr
    }

    fun setHashCharset(hashCharset: String) {
        this.hashCharsetStr = hashCharset
    }
}
