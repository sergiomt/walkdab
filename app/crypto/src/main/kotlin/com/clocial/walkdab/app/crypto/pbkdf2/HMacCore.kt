package com.clocial.walkdab.app.crypto.pbkdf2

/*
 * Copyright (c) 2002, 2009, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * This class constitutes the core of HMAC-<MD> algorithms, where
 * <MD> can be SHA1 or MD5, etc. See RFC 2104 for spec.
 *
 * @author Jan Luehe
</MD></MD> */
internal class HMacCore : Cloneable {
    private val md: MsgDigest
    private val k_ipad: ByteArray? // inner padding - key XORd with ipad
    private val k_opad: ByteArray? // outer padding - key XORd with opad
    private var first = false // Is this the first data to be processed?

    private val blockLen: Int

    /**
     * Standard constructor, creates a new HmacCore instance using the
     * specified MessageDigest object.
     */
    constructor(md: MsgDigest, bl: Int) {
        this.md = md
        this.blockLen = bl
        this.k_ipad = ByteArray(blockLen)
        this.k_opad = ByteArray(blockLen)
        first = true
    }

    /**
     * Standard constructor, creates a new HmacCore instance instantiating
     * a MessageDigest of the specified name.
     */
    constructor(digestAlgorithm: String, bl: Int) : this(MsgDigest.getInstance(digestAlgorithm), bl)

    /**
     * Constructor used for cloning.
     */
    private constructor(other: HMacCore) {
        this.md = other.md.clone() as MsgDigest
        this.blockLen = other.blockLen
        this.k_ipad = other.k_ipad?.clone()
        this.k_opad = other.k_opad?.clone()
        this.first = other.first
    }

    /**
     * Returns the length of the HMAC in bytes.
     *
     * @return the HMAC length in bytes.
     */
    fun getDigestLength(): Int {
        return md.digestLength
    }

    /**
     * Initializes the HMAC with the given secret key and algorithm parameters.
     *
     * @param secret the secret key.
     */
    fun init(secret: ByteArray) {
        // if key is longer than the block length, reset it using
        // the message digest object.

        var secret = secret
        if (secret.size > blockLen) {
            val tmp: ByteArray = md.digest(secret)
            // now erase the secret
            secret.fill(0)
            secret = tmp
        }

        // XOR k with ipad and opad, respectively
        for (i in 0 until blockLen) {
            val si = (if ((i < secret.size)) secret.get(i) else 0).toInt()
            k_ipad?.set(i, (si xor 0x36).toByte())
            k_opad?.set(i, (si xor 0x5c).toByte())
        }

        // now erase the secret
        secret.fill(0)

        reset()
    }

    /**
     * Processes the given byte.
     *
     * @param input the input byte to be processed.
     */
    fun update(input: Byte) {
        if (first == true) {
            // compute digest for 1st pass; start with inner pad
            if (k_ipad != null) {
                md.update(k_ipad)
            }
            first = false
        }

        // add the passed byte to the inner digest
        md.update(input)
    }

    /**
     * Processes the first `len` bytes in `input`,
     * starting at `offset`.
     *
     * @param input the input buffer.
     * @param offset the offset in `input` where the input starts.
     * @param len the number of bytes to process.
     */
    fun update(input: ByteArray, offset: Int, len: Int) {
        if (first == true) {
            // compute digest for 1st pass; start with inner pad
            if (k_ipad != null) {
                md.update(k_ipad)
            }
            first = false
        }

        // add the selected part of an array of bytes to the inner digest
        md.update(input, offset, len)
    }

    /**
     * Completes the HMAC computation and resets the HMAC for further use,
     * maintaining the secret key that the HMAC was initialized with.
     *
     * @return the HMAC result.
     */
    fun doFinal(): ByteArray {
        if (first == true) {
            // compute digest for 1st pass; start with inner pad
            if (k_ipad != null) {
                md.update(k_ipad)
            }
        } else {
            first = true
        }


        // finish the inner digest
        val tmp: ByteArray = md.digest()

        // compute digest for 2nd pass; start with outer pad
        if (k_opad != null) {
            md.update(k_opad)
        }
        // add result of 1st hash
        md.update(tmp)

        md.digest(tmp, 0, tmp.size)
        return tmp
    }

    /**
     * Resets the HMAC for further use, maintaining the secret key that the
     * HMAC was initialized with.
     */
    fun reset() {
        if (first == false) {
            md.reset()
            first = true
        }
    }

    public override fun clone(): Any {
        return HMacCore(this)
    }
}

