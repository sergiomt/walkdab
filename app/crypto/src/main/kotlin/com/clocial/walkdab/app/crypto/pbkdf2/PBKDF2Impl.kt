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

import kotlin.random.Random

/**
 * PBKDF2 convenience object that comes pre-configured.
 *
 *  * Salt Generator: &quot;SHA1PRNG&quot;
 *  * Hash Algorithm: &quot;HmacSHA1&quot;
 *  * Iterations: 2000
 *  * Encoding: &quot;ISO-8859-1&quot;
 *
 * Note: this class is **not thread-safe**. Create a new instance for each thread.
 *
 * @see [RFC 2898](http://tools.ietf.org/html/rfc2898)
 *
 * @author Matthias Grtner
 */
class PBKDF2Impl (saltSizeBytes: Int,
                  iterationCount: Int,
                  parameters: PBKDF2Parameters?) :
    PBKDF2Base(parameters ?: PBKDF2Parameters(
                "SHA256", "ISO-8859-1",
                null, (if (iterationCount < 0) 0 else iterationCount))
    ) {

    private val saltSize: Int = saltSizeBytes

    private val sr: Random = Random.Default // SecureRandom.getInstance("SHA1PRNG")

    private val formatter: PBKDF2HexFormatter = PBKDF2HexFormatter()

    fun getFormatter(): PBKDF2HexFormatter {
        return formatter
    }

    fun getSaltSize(): Int {
        return saltSize
    }

    /**
     * Derive key from password, then format.
     *
     * @param inputPassword The password to derive key from.
     * @return &quot;salt:iteration-count:derived-key&quot; (depends on effective formatter)
     */
    fun deriveKeyFormatted(inputPassword: String): String {
        val p: PBKDF2Parameters? = getParameters()
        if (p!=null) {
            val salt = if (p.getSalt()!=null) p.getSalt() else generateSalt()
            p.setSalt(salt)
            p.setDerivedKey(deriveKey(inputPassword))
            val formatted: String = getFormatter().toString(p)
            return formatted
        } else {
            return ""
        }
    }

    /**
     * Generate Salt. Default is 8 Bytes obtained from Random
     *
     * @return Random Bytes
     */
    protected fun generateSalt(): ByteArray? {
        val salt = ByteArray(getSaltSize())
        sr.nextBytes(salt)
        return salt
    }

    /**
     * Verification function.
     *
     * @param formatted
     * &quot;salt:iteration-count:derived-key&quot; (depends on
     * effective formatter). This value should come from server-side
     * storage.
     * @param candidatePassword
     * The password that is checked against the formatted reference
     * data. This value will usually be supplied by the
     * &quot;user&quot; or &quot;client&quot;.
     * @return `true` verification OK. `false`
     * verification failed or formatter unable to decode input value as
     * PBKDF2 parameters.
     */
    fun verifyKeyFormatted(formatted: String, candidatePassword: String): Boolean {
        // Parameter as member of Engine was not the smartest design decision back then...
        val p: PBKDF2Parameters? = getParameters()
        if (p!=null) {
            val q = PBKDF2Parameters()
            q.setHashAlgorithm(p.getHashAlgorithm())
            q.setHashCharset(p.getHashCharset())
            var verifyOK = false
            if (!getFormatter().fromString(q, formatted)) {
                try {
                    setParameters(q)
                    verifyOK = verifyKey(candidatePassword)
                } finally {
                    setParameters(p)
                }
            }
            return verifyOK
        } else {
            return false
        }
    }
}
