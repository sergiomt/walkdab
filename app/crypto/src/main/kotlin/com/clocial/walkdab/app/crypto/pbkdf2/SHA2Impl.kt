package com.clocial.walkdab.app.crypto.pbkdf2

/**
 * This class implements the Secure Hash Algorithm SHA-256 developed by
 * the National Institute of Standards and Technology along with the
 * National Security Agency.
 *
 */
internal class SHA2Impl (
    name: String, digestLength: Int,
    private val initialHashes: IntArray?
) : MsgDigestSpiBase(name, digestLength, 64) {

    // buffer used by implCompress()
    private var W: IntArray = IntArray(64)

    // state of this object
    private var state: IntArray

    internal class BE {
        internal object INT_ARRAY {
            fun set(out: ByteArray, outOffset: Int, input: Int) {
                out.set(outOffset, input.toByte())
            }

            fun get(source: ByteArray, offset: Int): Int {
                return source.get(offset).toInt()
            }
        }
    }

    /**
     * Creates a new SHA object.
     */
    init {
        state = IntArray(8)
        resetHashes()
    }

    /**
     * Resets the buffers and hash value to start a new hash.
     */
    override fun implReset() {
        resetHashes()
        if (W != null) {
            W.fill(0)
        }
    }

    private fun resetHashes() {
        System.arraycopy(initialHashes, 0, state, 0, state.size)
    }

    override fun implDigest(out: ByteArray, ofs: Int) {
        val bitsProcessed: Int = bytesProcessed shl 3

        val index = bytesProcessed as Int and 0x3f
        val padLen = if ((index < 56)) (56 - index) else (120 - index)
        engineUpdate(padding, 0, padLen)

        i2bBig4((bitsProcessed ushr 32).toInt(), buffer, 56)
        i2bBig4(bitsProcessed.toInt(), buffer, 60)
        implCompress(buffer, 0)

        i2bBig(state, 0, out, ofs, engineGetDigestLength())
    }

    /**
     * Process the current block to update the state variable state.
     */
    override fun implCompress(buf: ByteArray, ofs: Int) {
        implCompressCheck(buf, ofs)
        implCompress0(buf, ofs)
    }

    private fun implCompressCheck(buf: ByteArray, ofs: Int) {

        // Checks similar to those performed by the method 'b2iBig64'
        // are sufficient for the case when the method 'implCompress0' is
        // replaced with a compiler intrinsic.
        if (ofs < 0 || (buf.size - ofs) < 64) {
            throw ArrayIndexOutOfBoundsException()
        }
    }

    // The method 'implCompressImpl' seems not to use its parameters.
    // The method can, however, be replaced with a compiler intrinsic
    // that operates directly on the array 'buf' (starting from
    // offset 'ofs') and not on array 'W', therefore 'buf' and 'ofs'
    // must be passed as parameter to the method.
    private fun implCompress0(buf: ByteArray, ofs: Int) {
        if (W == null) {
            W = IntArray(64)
        }
        b2iBig64(buf, ofs, W)
        // The first 16 ints are from the byte stream, compute the rest of
        // the W[]'s
        for (t in 16 until ITERATION) {
            val W_t2 = W.get(t - 2)
            val W_t15 = W.get(t - 15)

            val delta0_W_t15 =
                ((W_t15 ushr 7) or (W_t15 shl 25)) xor
                        ((W_t15 ushr 18) or (W_t15 shl 14)) xor
                        (W_t15 ushr 3)

            // delta1(x) = S(x, 17) ^ S(x, 19) ^ R(x, 10)
            val delta1_W_t2 =
                ((W_t2 ushr 17) or (W_t2 shl 15)) xor
                        ((W_t2 ushr 19) or (W_t2 shl 13)) xor
                        (W_t2 ushr 10)

            W.set(t, delta0_W_t15 + delta1_W_t2 + W.get(t - 7) + W.get(t - 16))
        }

        var a = state.get(0)
        var b = state.get(1)
        var c = state.get(2)
        var d = state.get(3)
        var e = state.get(4)
        var f = state.get(5)
        var g = state.get(6)
        var h = state.get(7)

        for (i in 0 until ITERATION) {
            // S(x,s) is right rotation of x by s positions:
            //   S(x,s) = (x >>> s) | (x << (32 - s))

            // sigma0(x) = S(x,2) xor S(x,13) xor S(x,22)

            val sigma0_a =
                ((a ushr 2) or (a shl 30)) xor
                        ((a ushr 13) or (a shl 19)) xor
                        ((a ushr 22) or (a shl 10))

            // sigma1(x) = S(x,6) xor S(x,11) xor S(x,25)
            val sigma1_e =
                ((e ushr 6) or (e shl 26)) xor
                        ((e ushr 11) or (e shl 21)) xor
                        ((e ushr 25) or (e shl 7))

            // ch(x,y,z) = (x and y) xor ((complement x) and z)
            val ch_efg = (e and f) xor ((e.inv()) and g)

            // maj(x,y,z) = (x and y) xor (x and z) xor (y and z)
            val maj_abc = (a and b) xor (a and c) xor (b and c)

            val T1 = h + sigma1_e + ch_efg + ROUND_CONSTS.get(i) + W.get(i)
            val T2 = sigma0_a + maj_abc
            h = g
            g = f
            f = e
            e = d + T1
            d = c
            c = b
            b = a
            a = T1 + T2
        }

        state.set(0, state.get(0) + a)
        state.set(1, state.get(1) + b)
        state.set(2, state.get(2) + c)
        state.set(3, state.get(3) + d)
        state.set(4, state.get(4) + e)
        state.set(5, state.get(5) + f)
        state.set(6, state.get(6) + g)
        state.set(7, state.get(7) + h)
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        val copy = super.clone() as SHA2Impl
        copy.state = copy.state.clone()
        return copy
    }

    companion object {
        private const val ITERATION = 64

        // Constants for each round
        private val ROUND_CONSTS: IntArray = intArrayOf(
            0x428a2f98, 0x71374491, -0x4a3f0431, -0x164a245b,
            0x3956c25b, 0x59f111f1, -0x6dc07d5c, -0x54e3a12b,
            -0x27f85568, 0x12835b01, 0x243185be, 0x550c7dc3,
            0x72be5d74, -0x7f214e02, -0x6423f959, -0x3e640e8c,
            -0x1b64963f, -0x1041b87a, 0x0fc19dc6, 0x240ca1cc,
            0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            -0x67c1aeae, -0x57ce3993, -0x4ffcd838, -0x40a68039,
            -0x391ff40d, -0x2a586eb9, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
            0x650a7354, 0x766a0abb, -0x7e3d36d2, -0x6d8dd37b,
            -0x5d40175f, -0x57e599b5, -0x3db47490, -0x3893ae5d,
            -0x2e6d17e7, -0x2966f9dc, -0xbf1ca7b, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
            0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, -0x7b3787ec, -0x7338fdf8,
            -0x6f410006, -0x5baf9315, -0x41065c09, -0x398e870e
        )

        fun i2bBig(input: IntArray, inputOfs: Int, out: ByteArray, outputOfs: Int, len: Int) {
            var inOfs = inputOfs
            var outOfs = outputOfs
            val l = len + outputOfs
            while (outOfs < l) {
                BE.INT_ARRAY.set(out, outOfs, input.get(inOfs++))
                outOfs += 4
            }
        }

        fun i2bBig4(intValue: Int, out: ByteArray, outOfs: Int) {
            BE.INT_ARRAY.set(out, outOfs, intValue)
        }

        fun b2iBig64(input: ByteArray, inOfs: Int, out: IntArray) {
            out.set(0 , BE.INT_ARRAY.get(input, inOfs))
            out.set(1 , BE.INT_ARRAY.get(input, inOfs + 4))
            out.set(2 , BE.INT_ARRAY.get(input, inOfs + 8))
            out.set(3 , BE.INT_ARRAY.get(input, inOfs + 12))
            out.set(4 , BE.INT_ARRAY.get(input, inOfs + 16))
            out.set(5 , BE.INT_ARRAY.get(input, inOfs + 20))
            out.set(6 , BE.INT_ARRAY.get(input, inOfs + 24))
            out.set(7 , BE.INT_ARRAY.get(input, inOfs + 28))
            out.set(8 , BE.INT_ARRAY.get(input, inOfs + 32))
            out.set(9 , BE.INT_ARRAY.get(input, inOfs + 36))
            out.set(10, BE.INT_ARRAY.get(input, inOfs + 40))
            out.set(11, BE.INT_ARRAY.get(input, inOfs + 44))
            out.set(12, BE.INT_ARRAY.get(input, inOfs + 48))
            out.set(13, BE.INT_ARRAY.get(input, inOfs + 52))
            out.set(14, BE.INT_ARRAY.get(input, inOfs + 56))
            out.set(15, BE.INT_ARRAY.get(input, inOfs + 60))
        }
    }
}

