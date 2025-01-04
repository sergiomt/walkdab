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
 * Interface to **Password Based Key Derivation Function 2** implementations.
 *
 * @author Matthias Grtner
 * @since 1.0
 */
interface PBKDF2 {
    /**
     * Convert String-based input to internal byte array, then invoke PBKDF2.
     * Desired key length defaults to Pseudo Random Function block size.
     *
     * @param inputPassword
     * Candidate password to compute the derived key for.
     * @return internal byte array
     */
    fun deriveKey(inputPassword: String): ByteArray

    /**
     * Convert String-based input to internal byte array, then invoke PBKDF2.
     *
     * @param inputPassword
     * Candidate password to compute the derived key for.
     * @param dkLen
     * Specify desired key length
     * @return internal byte array
     */
    fun deriveKey(inputPassword: String, dkLen: Int): ByteArray

    /**
     * Convert String-based input to internal byte arrays, then invoke PBKDF2
     * and verify result against the reference data that is supplied in the
     * PBKDF2Parameters.
     *
     * @param inputPassword
     * Candidate password to compute the derived key for.
     * @return `true` password match; `false`
     * incorrect password
     */
    fun verifyKey(inputPassword: String): Boolean

    /**
     * Allow reading of configured parameters.
     *
     * @return Currently set parameters.
     */
    fun getParameters(): PBKDF2Parameters

    /**
     * Get currently set Pseudo Random Function.
     *
     * @return Currently set Pseudo Random Function
     */
    fun getPseudoRandomFunction(): PRF

}
