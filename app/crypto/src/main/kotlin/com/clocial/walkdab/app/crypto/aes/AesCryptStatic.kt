package com.clocial.walkdab.app.crypto.aes

class AesCryptStatic {
    val AES_BLOCK_SIZE: Int = 16
    val AES_KEYSIZES: IntArray = intArrayOf(16, 24, 32)
    private var alog: IntArray? = IntArray(256)
    private var log: IntArray? = IntArray(256)
    val S: ByteArray = ByteArray(256)
    val Si: ByteArray = ByteArray(256)
    val T1: IntArray = IntArray(256)
    val T2: IntArray = IntArray(256)
    val T3: IntArray = IntArray(256)
    val T4: IntArray = IntArray(256)
    val T5: IntArray = IntArray(256)
    val T6: IntArray = IntArray(256)
    val T7: IntArray = IntArray(256)
    val T8: IntArray = IntArray(256)
    val U1: IntArray = IntArray(256)
    val U2: IntArray = IntArray(256)
    val U3: IntArray = IntArray(256)
    val U4: IntArray = IntArray(256)
    val rcon: ByteArray = ByteArray(30)

    init {
        initialize()
    }

    fun expandToSubKey(kr: Array<IntArray>, decrypting: Boolean): IntArray {
        val total = kr.size
        val expK = IntArray(total * 4)
        var i: Int
        var j: Int
        if (decrypting) {
            i = 0
            while (i < 4) {
                expK[i] = kr[total - 1][i]
                ++i
            }

            i = 1
            while (i < total) {
                j = 0
                while (j < 4) {
                    expK[i * 4 + j] = kr[i - 1][j]
                    ++j
                }
                ++i
            }
        } else {
            i = 0
            while (i < total) {
                j = 0
                while (j < 4) {
                    expK[i * 4 + j] = kr[i][j]
                    ++j
                }
                ++i
            }
        }

        return expK
    }

    private fun mul(a: Int, b: Int): Int {
        return if (a != 0 && b != 0) alog!![(log!![a and 255] + log!![b and 255]) % 255] else 0
    }

    private fun mul4(a: Int, b: ByteArray): Int {
        var a = a
        if (a == 0) {
            return 0
        } else {
            a = log!![a and 255]
            val a0 = if (b[0].toInt() != 0) alog!![(a + log!![b[0].toInt() and 255]) % 255] and 255 else 0
            val a1 = if (b[1].toInt() != 0) alog!![(a + log!![b[1].toInt() and 255]) % 255] and 255 else 0
            val a2 = if (b[2].toInt() != 0) alog!![(a + log!![b[2].toInt() and 255]) % 255] and 255 else 0
            val a3 = if (b[3].toInt() != 0) alog!![(a + log!![b[3].toInt() and 255]) % 255] and 255 else 0
            return a0 shl 24 or (a1 shl 16) or (a2 shl 8) or a3
        }
    }

    fun isKeySizeValid(len: Int): Boolean {
        for (i in AES_KEYSIZES.indices) {
            if (len == AES_KEYSIZES[i]) {
                return true
            }
        }

        return false
    }

    fun getRounds(keySize: Int): Int {
        return (keySize shr 2) + 6
    }

    private fun initialize() {
        val ROOT = 283
        alog!![0] = 1
        var j: Int
        var i = 1
        while (i < 256) {
            j = alog!![i - 1] shl 1 xor alog!![i - 1]
            if ((j and 256) != 0) {
                j = j xor ROOT
            }

            alog!![i] = j
            ++i
        }

        i = 1
        while (i < 255) {
            log!![alog!![i]] = i
            i++
        }

        val A = arrayOf(
            byteArrayOf(1, 1, 1, 1, 1, 0, 0, 0),
            byteArrayOf(0, 1, 1, 1, 1, 1, 0, 0),
            byteArrayOf(0, 0, 1, 1, 1, 1, 1, 0),
            byteArrayOf(0, 0, 0, 1, 1, 1, 1, 1),
            byteArrayOf(1, 0, 0, 0, 1, 1, 1, 1),
            byteArrayOf(1, 1, 0, 0, 0, 1, 1, 1),
            byteArrayOf(1, 1, 1, 0, 0, 0, 1, 1),
            byteArrayOf(1, 1, 1, 1, 0, 0, 0, 1)
        )
        val B = byteArrayOf(0, 1, 1, 0, 0, 0, 1, 1)
        val box = Array(256) { ByteArray(8) }
        box[1][7] = 1

        var t: Int
        i = 2
        while (i < 256) {
            j = alog!![255 - log!![i]]

            t = 0
            while (t < 8) {
                box[i][t] = (j ushr 7 - t and 1).toByte()
                ++t
            }
            ++i
        }

        val cox = Array(256) { ByteArray(8) }

        i = 0
        while (i < 256) {
            t = 0
            while (t < 8) {
                cox[i][t] = B[t]

                j = 0
                while (j < 8) {
                    cox[i][t] = (cox[i][t].toInt() xor A[t][j] * box[i][j]).toByte()
                    ++j
                }
                ++t
            }
            ++i
        }

        i = 0
        while (i < 256) {
            S[i] = (cox[i][0].toInt() shl 7).toByte()

            t = 1
            while (t < 8) {
                val var10000 = S
                var10000[i] = (var10000[i].toInt() xor (cox[i][t].toInt() shl 7 - t)).toByte()
                ++t
            }

            Si[S[i].toInt() and 255] = i.toByte()
            ++i
        }

        val G =
            arrayOf(byteArrayOf(2, 1, 1, 3), byteArrayOf(3, 2, 1, 1), byteArrayOf(1, 3, 2, 1), byteArrayOf(1, 1, 3, 2))
        val AA = Array(4) { ByteArray(8) }

        i = 0
        while (i < 4) {
            j = 0
            while (j < 4) {
                AA[i][j] = G[i][j]
                ++j
            }

            AA[i][i + 4] = 1
            ++i
        }

        val iG = Array(4) { ByteArray(4) }

        i = 0
        while (i < 4) {
            var pivot = AA[i][i]
            if (pivot.toInt() == 0) {
                t = i + 1
                while (AA[t][i].toInt() == 0 && t < 4) {
                    ++t
                }

                if (t == 4) {
                    throw RuntimeException("G matrix is not invertible")
                }

                j = 0
                while (j < 8) {
                    val tmp = AA[i][j]
                    AA[i][j] = AA[t][j]
                    AA[t][j] = tmp
                    ++j
                }

                pivot = AA[i][i]
            }

            j = 0
            while (j < 8) {
                if (AA[i][j].toInt() != 0) {
                    AA[i][j] =
                        alog!![(255 + log!![AA[i][j].toInt() and 255] - log!![pivot.toInt() and 255]) % 255].toByte()
                }
                ++j
            }

            t = 0
            while (t < 4) {
                if (i != t) {
                    j = i + 1
                    while (j < 8) {
                        AA[t][j] = (AA[t][j].toInt() xor mul(AA[i][j].toInt(), AA[t][i].toInt())).toByte()
                        ++j
                    }

                    AA[t][i] = 0
                }
                ++t
            }
            ++i
        }

        i = 0
        while (i < 4) {
            j = 0
            while (j < 4) {
                iG[i][j] = AA[i][j + 4]
                ++j
            }
            ++i
        }

        t = 0
        while (t < 256) {
            var s = S[t].toInt()
            T1[t] = mul4(s, G[0])
            T2[t] = mul4(s, G[1])
            T3[t] = mul4(s, G[2])
            T4[t] = mul4(s, G[3])
            s = Si[t].toInt()
            T5[t] = mul4(s, iG[0])
            T6[t] = mul4(s, iG[1])
            T7[t] = mul4(s, iG[2])
            T8[t] = mul4(s, iG[3])
            U1[t] = mul4(t, iG[0])
            U2[t] = mul4(t, iG[1])
            U3[t] = mul4(t, iG[2])
            U4[t] = mul4(t, iG[3])
            ++t
        }

        rcon[0] = 1
        var r = 1

        t = 1
        while (t < 30) {
            r = mul(2, r)
            rcon[t] = r.toByte()
            ++t
        }

        log = null
        alog = null
    }

    fun isEqual(digesta: ByteArray?, digestb: ByteArray?): Boolean {
        if (digesta == digestb) return true
        if (digesta == null || digestb == null) {
            return false
        }

        val lenA = digesta.size
        val lenB = digestb.size

        if (lenB == 0) {
            return lenA == 0
        }

        var result = 0
        result = result or lenA - lenB

        // time-constant comparison
        for (i in 0 until lenA) {
            // If i >= lenB, indexB is 0; otherwise, i.
            val indexB = ((i - lenB) ushr 31) * i
            result = result or (digesta[i].toInt() xor digestb[indexB].toInt())
        }
        return result == 0
    }
}
