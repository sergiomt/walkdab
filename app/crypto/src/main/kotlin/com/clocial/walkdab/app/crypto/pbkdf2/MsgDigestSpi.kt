package com.clocial.walkdab.app.crypto.pbkdf2

/*
 * Copyright (c) 1997, 2006, Oracle and/or its affiliates. All rights reserved.
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

abstract class MsgDigestSpi {

    /**
     * Returns the digest length in bytes.
     *
     *
     * This concrete method has been added to this previously-defined
     * abstract class. (For backwards compatibility, it cannot be abstract.)
     *
     *
     * The default behavior is to return 0.
     *
     *
     * This method may be overridden by a provider to return the digest
     * length.
     *
     * @return the digest length in bytes.
     *
     * @since 1.2
     */
    abstract fun engineGetDigestLength(): Int

    /**
     * Updates the digest using the specified byte.
     *
     * @param input the byte to use for the update.
     */
    abstract fun engineUpdate(input: Byte)

    /**
     * Updates the digest using the specified array of bytes,
     * starting at the specified offset.
     *
     * @param input the array of bytes to use for the update.
     *
     * @param offset the offset to start from in the array of bytes.
     *
     * @param len the number of bytes to use, starting at
     * `offset`.
     */
    abstract fun engineUpdate(input: ByteArray, offset: Int, len: Int)

    /**
     * Completes the hash computation by performing final
     * operations such as padding. Once `engineDigest` has
     * been called, the engine should be reset (see
     * [engineReset][.engineReset]).
     * Resetting is the responsibility of the
     * engine implementor.
     *
     * @return the array of bytes for the resulting hash value.
     */
    abstract fun engineDigest(): ByteArray

    /**
     * Completes the hash computation by performing final
     * operations such as padding. Once `engineDigest` has
     * been called, the engine should be reset (see
     * [engineReset][.engineReset]).
     * Resetting is the responsibility of the
     * engine implementor.
     *
     * This method should be abstract, but we leave it concrete for
     * binary compatibility.  Knowledgeable providers should override this
     * method.
     *
     * @param buf the output buffer in which to store the digest
     *
     * @param offset offset to start from in the output buffer
     *
     * @param len number of bytes within buf allotted for the digest.
     * Both this default implementation and the SUN provider do not
     * return partial digests.  The presence of this parameter is solely
     * for consistency in our API's.  If the value of this parameter is less
     * than the actual digest length, the method will throw a IllegalArgumentException.
     * This parameter is ignored if its value is greater than or equal to
     * the actual digest length.
     *
     * @return the length of the digest stored in the output buffer.
     *
     * @exception IllegalArgumentException if an error occurs.
     *
     * @since 1.2
     */
    @Throws(IllegalArgumentException::class)
    abstract fun engineDigest(buf: ByteArray, offset: Int, len: Int): Int

    /**
     * Resets the digest for further use.
     */
    abstract fun engineReset()

}

