package com.clocial.walkdab.app.crypto.pbkdf2

/**
 * This MsgDigest class provides applications the functionality of a
 * message digest algorithm, such as SHA-1 or SHA-256.
 * Message digests are secure one-way hash functions that take arbitrary-sized
 * data and output a fixed-length hash value.
 *
 *
 * A MsgDigest object starts out initialized. The data is
 * processed through it using the [update][.update]
 * methods. At any point [reset][.reset] can be called
 * to reset the digest. Once all the data to be updated has been
 * updated, one of the [digest][.digest] methods should
 * be called to complete the hash computation.
 *
 *
 * The `digest` method can be called once for a given number
 * of updates. After `digest` has been called, the MsgDigest
 * object is reset to its initialized state.
 *
 *
 * Implementations are free to implement the Cloneable interface.
 * Client applications can test cloneability by attempting cloning
 * and catching the CloneNotSupportedException:
 *
 *
 *
 * <pre>
 * MsgDigest md = MsgDigest.getInstance("SHA");
 *
 * try {
 * md.update(toChapter1);
 * MsgDigest tc1 = md.clone();
 * byte[] toChapter1Digest = tc1.digest();
 * md.update(toChapter2);
 * ...etc.
 * } catch (CloneNotSupportedException cnse) {
 * throw new DigestException("couldn't make digest of partial content");
 * }
</pre> *
 *
 *
 * Note that if a given implementation is not cloneable, it is
 * still possible to compute intermediate digests by instantiating
 * several instances, if the number of digests is known in advance.
 *
 *
 * Note that this class is abstract and extends from
 * `MsgDigestSpi` for historical reasons.
 * Application developers should only take notice of the methods defined in
 * this `MsgDigest` class; all the methods in
 * the superclass are intended for cryptographic service providers who wish to
 * supply their own implementations of message digest algorithms.
 *
 *
 *  Every implementation of the Java platform is required to support
 * the following standard `MsgDigest` algorithms:
 *
 *  * <tt>MD5</tt>
 *  * <tt>SHA-1</tt>
 *  * <tt>SHA-256</tt>
 *
 * These algorithms are described in the [
 * MsgDigest section]({@docRoot}/../technotes/guides/security/StandardNames.html#MsgDigest) of the
 * Java Cryptography Architecture Standard Algorithm Name Documentation.
 * Consult the release documentation for your implementation to see if any
 * other algorithms are supported.
 *
 * @author Benjamin Renaud
 */
abstract class MsgDigest
/**
 * Creates a message digest with the specified algorithm name.
 *
 * @param algorithm the standard name of the digest algorithm.
 * See the MsgDigest section in the [
 * Java Cryptography Architecture Standard Algorithm Name Documentation]({@docRoot}/../technotes/guides/security/StandardNames.html#MsgDigest)
 * for information about standard algorithm names.
 */ protected constructor(
    /**
     * Returns a string that identifies the algorithm, independent of
     * implementation details. The name should be a standard
     * Java Security name (such as "SHA", "MD5", and so on).
     * See the MsgDigest section in the [
     * Java Cryptography Architecture Standard Algorithm Name Documentation]({@docRoot}/../technotes/guides/security/StandardNames.html#MsgDigest)
     * for information about standard algorithm names.
     *
     * @return SHA-224 or SHA-256
     */
    val algorithm: String
) : MsgDigestSpi(), Cloneable {
    private var state = INITIAL

    /**
     * Updates the digest using the specified byte.
     *
     * @param input the byte with which to update the digest.
     */
    fun update(input: Byte) {
        engineUpdate(input)
        state = IN_PROGRESS
    }

    /**
     * Updates the digest using the specified array of bytes, starting
     * at the specified offset.
     *
     * @param input the array of bytes.
     *
     * @param offset the offset to start from in the array of bytes.
     *
     * @param len the number of bytes to use, starting at
     * `offset`.
     */
    fun update(input: ByteArray?, offset: Int, len: Int) {
        requireNotNull(input) { "No input buffer given" }
        require(input.size - offset >= len) { "Input buffer too short" }
        engineUpdate(input, offset, len)
        state = IN_PROGRESS
    }

    /**
     * Updates the digest using the specified array of bytes.
     *
     * @param input the array of bytes.
     */
    fun update(input: ByteArray) {
        engineUpdate(input, 0, input.size)
        state = IN_PROGRESS
    }

    /**
     * Completes the hash computation by performing final operations
     * such as padding. The digest is reset after this call is made.
     *
     * @return the array of bytes for the resulting hash value.
     */
    fun digest(): ByteArray {
        /* Resetting is the responsibility of implementors. */
        val result = engineDigest()
        state = INITIAL
        return result
    }

    /**
     * Completes the hash computation by performing final operations
     * such as padding. The digest is reset after this call is made.
     *
     * @param buf output buffer for the computed digest
     *
     * @param offset offset into the output buffer to begin storing the digest
     *
     * @param len number of bytes within buf allotted for the digest
     *
     * @return the number of bytes placed into `buf`
     *
     * @exception IllegalArgumentException if an error occurs.
     */
    @Throws(IllegalArgumentException::class)
    fun digest(buf: ByteArray?, offset: Int, len: Int): Int {
        requireNotNull(buf) { "No output buffer given" }
        require(buf.size - offset >= len) { "Output buffer too small for specified offset and length" }
        val numBytes = engineDigest(buf, offset, len)
        state = INITIAL
        return numBytes
    }

    /**
     * Performs a final update on the digest using the specified array
     * of bytes, then completes the digest computation. That is, this
     * method first calls [update(input)][.update],
     * passing the *input* array to the `update` method,
     * then calls [digest()][.digest].
     *
     * @param input the input to be updated before the digest is
     * completed.
     *
     * @return the array of bytes for the resulting hash value.
     */
    fun digest(input: ByteArray): ByteArray {
        update(input)
        return digest()
    }

    /**
     * Returns a string representation of this message digest object.
     */
    /*
    override fun toString(): String {
        val baos = ByteArrayOutputStream()
        val p = PrintStream(baos)
        p.print("$algorithm Message Digest ")
        when (state) {
            INITIAL -> p.print("<initialized>")
            IN_PROGRESS -> p.print("<in progress>")
        }
        p.println()
        return (baos.toString())
    }
    */

    /**
     * Resets the digest for further use.
     */
    fun reset() {
        engineReset()
        state = INITIAL
    }

    val digestLength: Int
        /**
         * Returns the length of the digest in bytes
         *
         * @return the digest length in bytes
         *
         */
        get() {
            val digestLen = engineGetDigestLength()
            if (digestLen == 0) {
                try {
                    val md = clone() as MsgDigest
                    val digest = md.digest()
                    return digest.size
                } catch (e: CloneNotSupportedException) {
                    return digestLen
                }
            }
            return digestLen
        }

    public override fun clone(): Any {
        return super.clone()
    }

    internal class Delegate
        (
        private val digestSpi: MsgDigestSpi, algorithm: String
    ) : MsgDigest(algorithm) {
        private var tempArray: ByteArray? = null

        /**
         * Returns a clone if the delegate is cloneable.
         *
         * @return a clone if the delegate is cloneable.
         *
         */

        override fun clone(): Any {
            val digestSpiClone = (digestSpi as MsgDigestSpiBase).clone() as MsgDigestSpi
                val that: MsgDigest =
                    Delegate(digestSpiClone, (this as MsgDigest).algorithm)
                that.state = (this as MsgDigest).state
                return that

        }

        override fun engineGetDigestLength(): Int {
            return digestSpi.engineGetDigestLength()
        }

        override fun engineUpdate(input: Byte) {
            digestSpi.engineUpdate(input)
        }

        override fun engineUpdate(input: ByteArray, offset: Int, len: Int) {
            digestSpi.engineUpdate(input, offset, len)
        }

        /*
        override fun engineUpdate(input: ByteBuffer) {
            if (!input.hasRemaining()) {
                return
            }
            if (input.hasArray()) {
                val b = input.array()
                val ofs = input.arrayOffset()
                val pos = input.position()
                val lim = input.limit()
                engineUpdate(b, ofs + pos, lim - pos)
                input.position(lim)
            } else {
                var len = input.remaining()
                val n = min(4096.0, len.toDouble()).toInt()
                if ((tempArray == null) || (n > tempArray!!.size)) {
                    tempArray = ByteArray(n)
                }
                while (len > 0) {
                    val chunk = min(len.toDouble(), tempArray!!.size.toDouble()).toInt()
                    input[tempArray, 0, chunk]
                    engineUpdate(tempArray!!, 0, chunk)
                    len -= chunk
                }
            }
        }
        */

        override fun engineDigest(): ByteArray {
            return digestSpi.engineDigest()
        }

        @Throws(IllegalArgumentException::class)
        override fun engineDigest(buf: ByteArray, offset: Int, len: Int): Int {
            return digestSpi.engineDigest(buf, offset, len)
        }

        override fun engineReset() {
            digestSpi.engineReset()
        }
    }

    companion object {
        // The state of this digest
        private const val INITIAL = 0
        private const val IN_PROGRESS = 1

        /**
         * Returns a MsgDigest object that implements the specified digest algorithm.
         *
         * @param algorithm the name of the algorithm requested.
         *
         * @return a Message Digest object that implements the specified algorithm.
         *
         * @exception IllegalArgumentException if algorithm is not SHA224 or SHA256
         */
        @Throws(IllegalArgumentException::class)
        fun getInstance(algorithm: String?): MsgDigest {
            when (algorithm) {
                "SHA256" -> {
                    val algorithmSha256 = SHA2Impl(
                        "SHA-256", 32, intArrayOf(
                            0x6a09e667, -0x4498517b, 0x3c6ef372, -0x5ab00ac6,
                            0x510e527f, -0x64fa9774, 0x1f83d9ab, 0x5be0cd19
                        )
                    )
                    return Delegate(algorithmSha256, "SHA256")
                }

                "SHA224" -> {
                    val algorithmSha224 = SHA2Impl(
                        "SHA-224", 28, intArrayOf(
                            -0x3efa6128, 0x367cd507, 0x3070dd17, -0x8f1a6c7,
                            -0x3ff4cf, 0x68581511, 0x64f98fa7, -0x4105b05c
                        )
                    )
                    return Delegate(algorithmSha224, "SHA224")
                }

                else -> throw IllegalArgumentException(String.format("Unsupported algorithm %s", algorithm))
            }
        }

        /**
         * Compares two digests for equality. Does a simple byte compare.
         *
         * @param digesta one of the digests to compare.
         *
         * @param digestb the other digest to compare.
         *
         * @return true if the digests are equal, false otherwise.
         */
        fun isEqual(digesta: ByteArray, digestb: ByteArray): Boolean {
            if (digesta.size != digestb.size) {
                return false
            }

            var result = 0
            // time-constant comparison
            for (i in digesta.indices) {
                result = result or (digesta[i].toInt() xor digestb[i].toInt())
            }
            return result == 0
        }
    }
}
