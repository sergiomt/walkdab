package com.clocial.walkdab.app.crypto.pbkdf2

internal abstract class MsgDigestSpiBase(
    private val algorithm: String,
    private val digestLength: Int,
    private val blockSize: Int
) : MsgDigestSpi(), Cloneable {
    // one element byte array, temporary storage for update(byte)
    private var oneByte: ByteArray? = null

    // buffer to store partial blocks, blockSize bytes large
    // Subclasses should not access this array directly except possibly in their
    // implDigest() method. See MD5.java as an example.
    var buffer: ByteArray

    // offset into buffer
    private var bufOfs = 0

    // number of bytes processed so far. subclasses should not modify
    // this value.
    // also used as a flag to indicate reset status
    // -1: need to call engineReset() before next call to update()
    //  0: is already reset
    var bytesProcessed: Int = 0

    // return digest length. See JCA doc.
    override fun engineGetDigestLength(): Int {
        return digestLength
    }

    // single byte update. See JCA doc.
    override fun engineUpdate(input: Byte) {
        if (oneByte == null) {
            oneByte = ByteArray(1)
        }
        oneByte!![0] = input
        engineUpdate(oneByte!!, 0, 1)
    }

    // array update. See JCA doc.
    override fun engineUpdate(input: ByteArray, offset: Int, len: Int) {
        var o = offset
        var l = len
        if (l == 0) {
            return
        }
        if ((o < 0) || (l < 0) || (o > input.size - l)) {
            throw ArrayIndexOutOfBoundsException()
        }
        if (bytesProcessed < 0) {
            engineReset()
        }
        bytesProcessed += l
        // if buffer is not empty, we need to fill it before proceeding
        if (bufOfs != 0) {
            val n: Int = Math.min(l, blockSize - bufOfs)
            System.arraycopy(input, o, buffer, bufOfs, n)
            bufOfs += n
            o += n
            l -= n
            if (bufOfs >= blockSize) {
                // compress completed block now
                implCompress(buffer, 0)
                bufOfs = 0
            }
        }
        // compress complete blocks
        if (l >= blockSize) {
            val limit = o + l
            o = implCompressMultiBlock(input, o, limit - blockSize)
            l = limit - o
        }
        // copy remainder to buffer
        if (l > 0) {
            System.arraycopy(input, o, buffer, 0, l)
            bufOfs = l
        }
    }

    // compress complete blocks
    private fun implCompressMultiBlock(b: ByteArray, ofs: Int, limit: Int): Int {
        implCompressMultiBlockCheck(b, ofs, limit)
        return implCompressMultiBlock0(b, ofs, limit)
    }

    private fun implCompressMultiBlock0(b: ByteArray, ofs: Int, limit: Int): Int {
        var o = ofs
        while (o <= limit) {
            implCompress(b, o)
            o += blockSize
        }
        return o
    }

    private fun implCompressMultiBlockCheck(b: ByteArray, ofs: Int, limit: Int) {
        if (limit < 0) {
            return  // not an error because implCompressMultiBlockImpl won't execute if limit < 0
            // and an exception is thrown if ofs < 0.
        }

        if (ofs < 0 || ofs >= b.size) {
            throw ArrayIndexOutOfBoundsException(ofs)
        }

        val endIndex = (limit / blockSize) * blockSize + blockSize - 1
        if (endIndex >= b.size) {
            throw ArrayIndexOutOfBoundsException(endIndex)
        }
    }

    override fun engineReset() {
        if (bytesProcessed == 0) {
            // already reset, ignore
            return
        }
        implReset()
        bufOfs = 0
        bytesProcessed = 0
        buffer.fill(0)
    }

    override fun engineDigest(): ByteArray {
        val b = ByteArray(digestLength)
        engineDigest(b, 0, b.size)
        return b
    }

    override fun engineDigest(out: ByteArray, offset: Int, len: Int): Int {
        if (len < digestLength) {
            throw IllegalArgumentException("Length must be at least " + digestLength + " for " + algorithm + "digests")
        }
        if ((offset < 0) || (len < 0) || (offset > out.size - len)) {
            throw ArrayIndexOutOfBoundsException("Buffer too short to store digest")
        }
        if (bytesProcessed < 0) {
            engineReset()
        }
        implDigest(out, offset)
        bytesProcessed = -1
        return digestLength
    }

    /**
     * Core compression function. Processes blockSize bytes at a time
     * and updates the state of this object.
     */
    abstract fun implCompress(b: ByteArray, ofs: Int)

    /**
     * Return the digest. Subclasses do not need to reset() themselves,
     * DigestBase calls implReset() when necessary.
     */
    abstract fun implDigest(out: ByteArray, ofs: Int)

    /**
     * Reset subclass specific state to their initial values. DigestBase
     * calls this method when necessary.
     */
    abstract fun implReset()

    public override fun clone(): Any {
        val copy = super.clone() as MsgDigestSpiBase
        copy.buffer = copy.buffer.clone()
        copy.oneByte = null
        return copy
    }

    /**
     * Main constructor.
     */
    init {
        buffer = ByteArray(blockSize)
    }

    companion object {
        // padding used for the MD5, and SHA-* message digests
        // we need 128 byte padding for SHA-384/512
        // and an additional 8 bytes for the high 8 bytes of the 16
        // byte bit counter in SHA-384/512
        val padding: ByteArray = ByteArray(136)

        init {
            padding[0] = 0x80.toByte()
        }
    }
}

