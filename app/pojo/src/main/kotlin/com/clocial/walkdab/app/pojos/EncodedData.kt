package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.encoding.BaseZ85
import com.clocial.walkdab.app.util.arrays.NullSafeArrays.arraysEquals

class EncodedData {

    val BASE85 = 85

    val base: Int

    private var encoded: String

    private var binary: ByteArray?

    constructor() {
        base = BASE85
        encoded = ""
        binary = null
    }

    constructor(encoded: String) {
        base = BASE85
        this.encoded = encoded
        binary = null
    }

    constructor(base: Int, encoded: String) {
        require(base == 85) { "Only base 85 is supported" }
        this.base = base
        this.encoded = encoded
        binary = null
    }

    constructor(input: ByteArray?) {
        base = BASE85
        encoded = ""
        binary = input
    }

    constructor(base: Int, input: ByteArray?) {
        require(base == 85) { "Only base 85 is supported" }
        this.base = base
        encoded = ""
        binary = input
    }

    fun isEmpty(): Boolean {
        return binary==null && encoded.isEmpty()
    }

    fun toBytes(): ByteArray {
        return binary ?: BaseZ85.decode(encoded)
    }

    override fun toString(): String {
        return if (encoded.isEmpty()) {
            val binNotNull: ByteArray = binary ?: ByteArray(0)
            return if (binNotNull.isEmpty()) "" else BaseZ85.encode(binNotNull)
        } else {
            encoded
        }
    }

    override fun hashCode(): Int {
        return  encoded.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is EncodedData) {
            if (base == other.base) {
                encoded == other.encoded
            } else if (binary != null && other.binary != null) {
                arraysEquals(binary, other.binary)
            } else if (binary != null) {
                arraysEquals(binary, other.toBytes())
            } else {
                arraysEquals(toBytes(), other.toBytes())
            }
        } else if (other is ByteArray) {
            return arraysEquals(toBytes(), other)
        } else {
            false
        }
    }

}