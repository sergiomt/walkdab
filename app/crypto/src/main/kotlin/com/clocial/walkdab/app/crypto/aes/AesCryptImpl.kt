package com.clocial.walkdab.app.crypto.aes

internal class AesCryptImpl(decrypting: Boolean, key: ByteArray) : SymmetricCiphering {

    private var ROUNDS_12 = false
    private var ROUNDS_14 = false
    private var sessionK: Array<IntArray>? = null
    private var K: IntArray? = null
    private var lastKey: ByteArray? = null
    private var limit = 0

    init {
        init(decrypting, key)
    }

    override fun getBlockSize(): Int {
        return A.AES_BLOCK_SIZE
    }

    @Throws(IllegalArgumentException::class)
    override fun init(decrypting: Boolean, key: ByteArray) {
        require(A.isKeySizeValid(key.size)) { "Invalid AES key length: " + key.size + " bytes" }
        if (!A.isEqual(key, lastKey)) {
            makeSessionKey(key)
            if (lastKey != null) {
                lastKey?.fill(0)
            }

            lastKey = key.clone()
        }

        K = sessionK!![if (decrypting) 1 else 0]
    }

    override fun encryptBlock(input: ByteArray, inOff: Int, output: ByteArray, outOff: Int) {
        var inOffset = inOff
        var outOffset = outOff

        var keyOffset = 0
        var t0 =
            (input[inOffset++].toInt() shl 24 or ((input[inOffset++].toInt() and 255) shl 16) or ((input[inOffset++].toInt() and 255) shl 8) or (input[inOffset++].toInt() and 255)) xor K!![keyOffset++]
        var t1 =
            (input[inOffset++].toInt() shl 24 or ((input[inOffset++].toInt() and 255) shl 16) or ((input[inOffset++].toInt() and 255) shl 8) or (input[inOffset++].toInt() and 255)) xor K!![keyOffset++]
        var t2 =
            (input[inOffset++].toInt() shl 24 or ((input[inOffset++].toInt() and 255) shl 16) or ((input[inOffset++].toInt() and 255) shl 8) or (input[inOffset++].toInt() and 255)) xor K!![keyOffset++]

        var t3: Int
        var tt: Int
        var a2: Int
        t3 =
            (input[inOffset++].toInt() shl 24 or ((input[inOffset++].toInt() and 255) shl 16) or ((input[inOffset++].toInt() and 255) shl 8) or (input[inOffset++].toInt() and 255)) xor K!![keyOffset++]
        while (keyOffset < limit) {
            tt =
                A.T1[t0 ushr 24] xor A.T2[t1 ushr 16 and 255] xor A.T3[t2 ushr 8 and 255] xor A.T4[t3 and 255] xor K!![keyOffset++]
            val a1 =
                A.T1[t1 ushr 24] xor A.T2[t2 ushr 16 and 255] xor A.T3[t3 ushr 8 and 255] xor A.T4[t0 and 255] xor K!![keyOffset++]
            a2 =
                A.T1[t2 ushr 24] xor A.T2[t3 ushr 16 and 255] xor A.T3[t0 ushr 8 and 255] xor A.T4[t1 and 255] xor K!![keyOffset++]
            t3 =
                A.T1[t3 ushr 24] xor A.T2[t0 ushr 16 and 255] xor A.T3[t1 ushr 8 and 255] xor A.T4[t2 and 255] xor K!![keyOffset++]
            t0 = tt
            t1 = a1
            t2 = a2
        }

        tt = K!![keyOffset++]
        output[outOffset++] = (A.S[t0 ushr 24].toInt() xor (tt ushr 24)).toByte()
        output[outOffset++] = (A.S[t1 ushr 16 and 255].toInt() xor (tt ushr 16)).toByte()
        output[outOffset++] = (A.S[t2 ushr 8 and 255].toInt() xor (tt ushr 8)).toByte()
        output[outOffset++] = (A.S[t3 and 255].toInt() xor tt).toByte()
        tt = K!![keyOffset++]
        output[outOffset++] = (A.S[t1 ushr 24].toInt() xor (tt ushr 24)).toByte()
        output[outOffset++] = (A.S[t2 ushr 16 and 255].toInt() xor (tt ushr 16)).toByte()
        output[outOffset++] = (A.S[t3 ushr 8 and 255].toInt() xor (tt ushr 8)).toByte()
        output[outOffset++] = (A.S[t0 and 255].toInt() xor tt).toByte()
        tt = K!![keyOffset++]
        output[outOffset++] = (A.S[t2 ushr 24].toInt() xor (tt ushr 24)).toByte()
        output[outOffset++] = (A.S[t3 ushr 16 and 255].toInt() xor (tt ushr 16)).toByte()
        output[outOffset++] = (A.S[t0 ushr 8 and 255].toInt() xor (tt ushr 8)).toByte()
        output[outOffset++] = (A.S[t1 and 255].toInt() xor tt).toByte()
        tt = K!![keyOffset++]
        output[outOffset++] = (A.S[t3 ushr 24].toInt() xor (tt ushr 24)).toByte()
        output[outOffset++] = (A.S[t0 ushr 16 and 255].toInt() xor (tt ushr 16)).toByte()
        output[outOffset++] = (A.S[t1 ushr 8 and 255].toInt() xor (tt ushr 8)).toByte()
        output[outOffset] = (A.S[t2 and 255].toInt() xor tt).toByte()
    }

    override fun decryptBlock(input: ByteArray, inOff: Int, output: ByteArray, outOff: Int) {
        var inOffset = inOff
        var outOffset = outOff

        var keyOffset = 4
        var t0 =
            (input[inOffset++].toInt() shl 24 or ((input[inOffset++].toInt() and 255) shl 16) or ((input[inOffset++].toInt() and 255) shl 8) or (input[inOffset++].toInt() and 255)) xor K!![keyOffset++]
        var t1 =
            (input[inOffset++].toInt() shl 24 or ((input[inOffset++].toInt() and 255) shl 16) or ((input[inOffset++].toInt() and 255) shl 8) or (input[inOffset++].toInt() and 255)) xor K!![keyOffset++]
        var t2 =
            (input[inOffset++].toInt() shl 24 or ((input[inOffset++].toInt() and 255) shl 16) or ((input[inOffset++].toInt() and 255) shl 8) or (input[inOffset++].toInt() and 255)) xor K!![keyOffset++]
        var t3 =
            (input[inOffset++].toInt() shl 24 or ((input[inOffset++].toInt() and 255) shl 16) or ((input[inOffset++].toInt() and 255) shl 8) or (input[inOffset].toInt() and 255)) xor K!![keyOffset++]
        var a0: Int
        var a1: Int
        var a2: Int
        if (ROUNDS_12) {
            a0 =
                A.T5[t0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[t2 ushr 8 and 255] xor A.T8[t1 and 255] xor K!![keyOffset++]
            a1 =
                A.T5[t1 ushr 24] xor A.T6[t0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[t2 and 255] xor K!![keyOffset++]
            a2 =
                A.T5[t2 ushr 24] xor A.T6[t1 ushr 16 and 255] xor A.T7[t0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
            t3 =
                A.T5[t3 ushr 24] xor A.T6[t2 ushr 16 and 255] xor A.T7[t1 ushr 8 and 255] xor A.T8[t0 and 255] xor K!![keyOffset++]
            t0 =
                A.T5[a0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[a2 ushr 8 and 255] xor A.T8[a1 and 255] xor K!![keyOffset++]
            t1 =
                A.T5[a1 ushr 24] xor A.T6[a0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[a2 and 255] xor K!![keyOffset++]
            t2 =
                A.T5[a2 ushr 24] xor A.T6[a1 ushr 16 and 255] xor A.T7[a0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
            t3 =
                A.T5[t3 ushr 24] xor A.T6[a2 ushr 16 and 255] xor A.T7[a1 ushr 8 and 255] xor A.T8[a0 and 255] xor K!![keyOffset++]
            if (ROUNDS_14) {
                a0 =
                    A.T5[t0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[t2 ushr 8 and 255] xor A.T8[t1 and 255] xor K!![keyOffset++]
                a1 =
                    A.T5[t1 ushr 24] xor A.T6[t0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[t2 and 255] xor K!![keyOffset++]
                a2 =
                    A.T5[t2 ushr 24] xor A.T6[t1 ushr 16 and 255] xor A.T7[t0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
                t3 =
                    A.T5[t3 ushr 24] xor A.T6[t2 ushr 16 and 255] xor A.T7[t1 ushr 8 and 255] xor A.T8[t0 and 255] xor K!![keyOffset++]
                t0 =
                    A.T5[a0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[a2 ushr 8 and 255] xor A.T8[a1 and 255] xor K!![keyOffset++]
                t1 =
                    A.T5[a1 ushr 24] xor A.T6[a0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[a2 and 255] xor K!![keyOffset++]
                t2 =
                    A.T5[a2 ushr 24] xor A.T6[a1 ushr 16 and 255] xor A.T7[a0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
                t3 =
                    A.T5[t3 ushr 24] xor A.T6[a2 ushr 16 and 255] xor A.T7[a1 ushr 8 and 255] xor A.T8[a0 and 255] xor K!![keyOffset++]
            }
        }

        a0 =
            A.T5[t0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[t2 ushr 8 and 255] xor A.T8[t1 and 255] xor K!![keyOffset++]
        a1 =
            A.T5[t1 ushr 24] xor A.T6[t0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[t2 and 255] xor K!![keyOffset++]
        a2 =
            A.T5[t2 ushr 24] xor A.T6[t1 ushr 16 and 255] xor A.T7[t0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[t2 ushr 16 and 255] xor A.T7[t1 ushr 8 and 255] xor A.T8[t0 and 255] xor K!![keyOffset++]
        t0 =
            A.T5[a0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[a2 ushr 8 and 255] xor A.T8[a1 and 255] xor K!![keyOffset++]
        t1 =
            A.T5[a1 ushr 24] xor A.T6[a0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[a2 and 255] xor K!![keyOffset++]
        t2 =
            A.T5[a2 ushr 24] xor A.T6[a1 ushr 16 and 255] xor A.T7[a0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[a2 ushr 16 and 255] xor A.T7[a1 ushr 8 and 255] xor A.T8[a0 and 255] xor K!![keyOffset++]
        a0 =
            A.T5[t0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[t2 ushr 8 and 255] xor A.T8[t1 and 255] xor K!![keyOffset++]
        a1 =
            A.T5[t1 ushr 24] xor A.T6[t0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[t2 and 255] xor K!![keyOffset++]
        a2 =
            A.T5[t2 ushr 24] xor A.T6[t1 ushr 16 and 255] xor A.T7[t0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[t2 ushr 16 and 255] xor A.T7[t1 ushr 8 and 255] xor A.T8[t0 and 255] xor K!![keyOffset++]
        t0 =
            A.T5[a0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[a2 ushr 8 and 255] xor A.T8[a1 and 255] xor K!![keyOffset++]
        t1 =
            A.T5[a1 ushr 24] xor A.T6[a0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[a2 and 255] xor K!![keyOffset++]
        t2 =
            A.T5[a2 ushr 24] xor A.T6[a1 ushr 16 and 255] xor A.T7[a0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[a2 ushr 16 and 255] xor A.T7[a1 ushr 8 and 255] xor A.T8[a0 and 255] xor K!![keyOffset++]
        a0 =
            A.T5[t0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[t2 ushr 8 and 255] xor A.T8[t1 and 255] xor K!![keyOffset++]
        a1 =
            A.T5[t1 ushr 24] xor A.T6[t0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[t2 and 255] xor K!![keyOffset++]
        a2 =
            A.T5[t2 ushr 24] xor A.T6[t1 ushr 16 and 255] xor A.T7[t0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[t2 ushr 16 and 255] xor A.T7[t1 ushr 8 and 255] xor A.T8[t0 and 255] xor K!![keyOffset++]
        t0 =
            A.T5[a0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[a2 ushr 8 and 255] xor A.T8[a1 and 255] xor K!![keyOffset++]
        t1 =
            A.T5[a1 ushr 24] xor A.T6[a0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[a2 and 255] xor K!![keyOffset++]
        t2 =
            A.T5[a2 ushr 24] xor A.T6[a1 ushr 16 and 255] xor A.T7[a0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[a2 ushr 16 and 255] xor A.T7[a1 ushr 8 and 255] xor A.T8[a0 and 255] xor K!![keyOffset++]
        a0 =
            A.T5[t0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[t2 ushr 8 and 255] xor A.T8[t1 and 255] xor K!![keyOffset++]
        a1 =
            A.T5[t1 ushr 24] xor A.T6[t0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[t2 and 255] xor K!![keyOffset++]
        a2 =
            A.T5[t2 ushr 24] xor A.T6[t1 ushr 16 and 255] xor A.T7[t0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[t2 ushr 16 and 255] xor A.T7[t1 ushr 8 and 255] xor A.T8[t0 and 255] xor K!![keyOffset++]
        t0 =
            A.T5[a0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[a2 ushr 8 and 255] xor A.T8[a1 and 255] xor K!![keyOffset++]
        t1 =
            A.T5[a1 ushr 24] xor A.T6[a0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[a2 and 255] xor K!![keyOffset++]
        t2 =
            A.T5[a2 ushr 24] xor A.T6[a1 ushr 16 and 255] xor A.T7[a0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[a2 ushr 16 and 255] xor A.T7[a1 ushr 8 and 255] xor A.T8[a0 and 255] xor K!![keyOffset++]
        a0 =
            A.T5[t0 ushr 24] xor A.T6[t3 ushr 16 and 255] xor A.T7[t2 ushr 8 and 255] xor A.T8[t1 and 255] xor K!![keyOffset++]
        a1 =
            A.T5[t1 ushr 24] xor A.T6[t0 ushr 16 and 255] xor A.T7[t3 ushr 8 and 255] xor A.T8[t2 and 255] xor K!![keyOffset++]
        a2 =
            A.T5[t2 ushr 24] xor A.T6[t1 ushr 16 and 255] xor A.T7[t0 ushr 8 and 255] xor A.T8[t3 and 255] xor K!![keyOffset++]
        t3 =
            A.T5[t3 ushr 24] xor A.T6[t2 ushr 16 and 255] xor A.T7[t1 ushr 8 and 255] xor A.T8[t0 and 255] xor K!![keyOffset++]
        t1 = K!![0]
        output[outOffset++] = (A.Si[a0 ushr 24].toInt() xor (t1 ushr 24)).toByte()
        output[outOffset++] = (A.Si[t3 ushr 16 and 255].toInt() xor (t1 ushr 16)).toByte()
        output[outOffset++] = (A.Si[a2 ushr 8 and 255].toInt() xor (t1 ushr 8)).toByte()
        output[outOffset++] = (A.Si[a1 and 255].toInt() xor t1).toByte()
        t1 = K!![1]
        output[outOffset++] = (A.Si[a1 ushr 24].toInt() xor (t1 ushr 24)).toByte()
        output[outOffset++] = (A.Si[a0 ushr 16 and 255].toInt() xor (t1 ushr 16)).toByte()
        output[outOffset++] = (A.Si[t3 ushr 8 and 255].toInt() xor (t1 ushr 8)).toByte()
        output[outOffset++] = (A.Si[a2 and 255].toInt() xor t1).toByte()
        t1 = K!![2]
        output[outOffset++] = (A.Si[a2 ushr 24].toInt() xor (t1 ushr 24)).toByte()
        output[outOffset++] = (A.Si[a1 ushr 16 and 255].toInt() xor (t1 ushr 16)).toByte()
        output[outOffset++] = (A.Si[a0 ushr 8 and 255].toInt() xor (t1 ushr 8)).toByte()
        output[outOffset++] = (A.Si[t3 and 255].toInt() xor t1).toByte()
        t1 = K!![3]
        output[outOffset++] = (A.Si[t3 ushr 24].toInt() xor (t1 ushr 24)).toByte()
        output[outOffset++] = (A.Si[a2 ushr 16 and 255].toInt() xor (t1 ushr 16)).toByte()
        output[outOffset++] = (A.Si[a1 ushr 8 and 255].toInt() xor (t1 ushr 8)).toByte()
        output[outOffset] = (A.Si[a0 and 255].toInt() xor t1).toByte()
    }

    @Throws(java.lang.IllegalArgumentException::class)
    private fun makeSessionKey(k: ByteArray?) {
        requireNotNull(k) { "Empty key" }
        require(A.isKeySizeValid(k.size)) { "Invalid AES key length: " + k.size + " bytes" }
        val ROUNDS: Int = A.getRounds(k.size)
        val ROUND_KEY_COUNT = (ROUNDS + 1) * 4
        val BC = 4
        val Ke = Array(ROUNDS + 1) { IntArray(4) }
        val Kd = Array(ROUNDS + 1) { IntArray(4) }
        val KC = k.size / 4
        val tk = IntArray(KC)
        var i = 0

        var j: Int
        j = 0
        while (i < KC) {
            tk[i] =
                k[j].toInt() shl 24 or ((k[j + 1].toInt() and 255) shl 16) or ((k[j + 2].toInt() and 255) shl 8) or (k[j + 3].toInt() and 255)
            ++i
            j += 4
        }

        var t = 0

        j = 0
        while (j < KC && t < ROUND_KEY_COUNT) {
            Ke[t / 4][t % 4] = tk[j]
            Kd[ROUNDS - t / 4][t % 4] = tk[j]
            ++j
            ++t
        }

        var rconpointer = 0

        var tt: Int
        while (t < ROUND_KEY_COUNT) {
            tt = tk[KC - 1]
            tk[0] =
                tk[0] xor (A.S.get(tt ushr 16 and 255).toInt() shl 24   xor ((A.S.get(tt ushr 8 and 255).toInt() and 255) shl 16)
                      xor ((A.S.get(tt and 255).toInt() and 255) shl 8) xor (A.S.get(tt ushr 24).toInt() and 255)
                      xor (A.rcon.get(rconpointer++).toInt() shl 24))
            if (KC != 8) {
                i = 1

                j = 0
                while (i < KC) {
                    tk[i] = tk[i] xor tk[j]
                    ++i
                    ++j
                }
            } else {
                i = 1

                j = 0
                while (i < KC / 2) {
                    tk[i] = tk[i] xor tk[j]
                    ++i
                    ++j
                }

                tt = tk[KC / 2 - 1]
                tk[KC / 2] =
                    tk[KC / 2] xor (A.S.get(tt and 255).toInt() and 255 xor ((A.S.get(tt ushr 8 and 255).toInt() and 255) shl 8)
                               xor ((A.S.get(tt ushr 16 and 255).toInt() and 255) shl 16) xor (A.S.get(tt ushr 24).toInt() shl 24))
                j = KC / 2

                i = j + 1
                while (i < KC) {
                    tk[i] = tk[i] xor tk[j]
                    ++i
                    ++j
                }
            }

            j = 0
            while (j < KC && t < ROUND_KEY_COUNT) {
                Ke[t / 4][t % 4] = tk[j]
                Kd[ROUNDS - t / 4][t % 4] = tk[j]
                ++j
                ++t
            }
        }

        for (r in 1 until ROUNDS) {
            j = 0
            while (j < BC) {
                tt = Kd[r][j]
                Kd[r][j] =
                    A.U1.get(tt ushr 24 and 255) xor A.U2.get(
                        tt ushr 16 and 255
                    ) xor A.U3.get(tt ushr 8 and 255) xor A.U4.get(
                        tt and 255
                    )
                ++j
            }
        }

        val expandedKe: IntArray = A.expandToSubKey(Ke, false)
        val expandedKd: IntArray = A.expandToSubKey(Kd, true)
        tk.fill(0)
        var var16 = Ke
        var var17 = Ke.size
        var ia: IntArray
        var var18 = 0
        while (var18 < var17) {
            ia = var16[var18]
            ia.fill(0)
            ++var18
        }

        var16 = Kd
        var17 = Kd.size

        var18 = 0
        while (var18 < var17) {
            ia = var16[var18]
            ia.fill(0)
            ++var18
        }

        ROUNDS_12 = ROUNDS >= 12
        ROUNDS_14 = ROUNDS == 14
        limit = ROUNDS * 4
        if (sessionK != null) {
            sessionK!![0].fill(0)
            sessionK!![1].fill(0)
        }

        sessionK = arrayOf(expandedKe, expandedKd)
    }

    companion object {
        private val A = AesCryptStatic()
    }
}

