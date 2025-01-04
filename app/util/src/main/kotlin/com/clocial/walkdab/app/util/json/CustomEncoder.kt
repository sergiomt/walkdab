package com.clocial.walkdab.app.util.json

class CustomEncoder : Encoder() {
    interface Encoder<T> {
        fun encode(buf: StringBuilder, r: Any?)
    }

    private val encoders: MutableMap<Class<*>, Encoder<*>> =
        HashMap()

    fun addEncoder(c: Class<*>, encoder: Encoder<*>) {
        encoders[c] = encoder
    }

    override fun canEncode(o: Any): Boolean {
        return encoders.containsKey(o.javaClass)
    }

    override fun encodeCustom(o: Any) {
        val encoder = encoders[o.javaClass]!!
        encoder.encode(this.buf, o)
    }
}
