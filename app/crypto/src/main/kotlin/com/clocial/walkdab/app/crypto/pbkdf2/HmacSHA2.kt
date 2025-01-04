package com.clocial.walkdab.app.crypto.pbkdf2

/*
 * Copyright (c) 1998, 2009, Oracle and/or its affiliates. All rights reserved.
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

import java.nio.ByteBuffer

class HmacSHA2 : MacSpi() {
    private var hmac: HMacCore = HMacCore(MsgDigest.getInstance("SHA256"), SHA2_BLOCK_LENGTH)

    /**
     * Returns the length of the HMAC in bytes.
     *
     * @return the HMAC length in bytes.
     */
    public override fun engineGetMacLength(): Int {
        return hmac.getDigestLength()
    }

    /**
     * Initializes the HMAC with the given secret key and algorithm parameters.
     *
     * @param secret the secret key.
     */
    public override fun engineInit(secret: ByteArray) {
        hmac.init(secret)
    }

    /**
     * Processes the given byte.
     *
     * @param input the input byte to be processed.
     */
    override fun engineUpdate(input: Byte) {
        hmac.update(input)
    }

    /**
     * Processes the first `len` bytes in `input`,
     * starting at `offset`.
     *
     * @param input the input buffer.
     * @param offset the offset in `input` where the input starts.
     * @param len the number of bytes to process.
     */
    public override fun engineUpdate(input: ByteArray, offset: Int, len: Int) {
        hmac.update(input, offset, len)
    }

    /*
    fun engineUpdateFromBuffer(input: ByteBuffer) {
        hmac.update(input)
    }
    */

    /**
     * Completes the HMAC computation and resets the HMAC for further use,
     * maintaining the secret key that the HMAC was initialized with.
     *
     * @return the HMAC result.
     */
    public override fun engineDoFinal(): ByteArray {
        return hmac.doFinal()
    }

    /**
     * Resets the HMAC for further use, maintaining the secret key that the
     * HMAC was initialized with.
     */
    public override fun engineReset() {
        hmac.reset()
    }

    override fun clone(): Any {
        val that: HmacSHA2 = super.clone() as HmacSHA2
        that.hmac = hmac.clone() as HMacCore
        return that
    }

    companion object {
        private const val SHA2_BLOCK_LENGTH = 64
    }
}
