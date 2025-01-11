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
 * Interface to Pseudorandom Function.
 *
 * @see [RFC 2898](http://tools.ietf.org/html/rfc2898)
 *
 * @author Matthias Gartner
 */
interface PRF {
    /**
     * Initialize this instance with the user-supplied password.
     *
     * @param secret
     * The password supplied as array of bytes. It is the caller's
     * task to convert String passwords to bytes as appropriate.
     */
    open fun init(secret: ByteArray)

    /**
     * Pseudo Random Function
     *
     * @param input
     * Input data/message etc. Together with any data supplied during initilization.
     * @return Random bytes of hLen length.
     */
    open fun doFinal(input: ByteArray): ByteArray

    /**
     * Query block size of underlying algorithm/mechanism.
     *
     * @return block size
     */
    open fun getHLen(): Int
}
