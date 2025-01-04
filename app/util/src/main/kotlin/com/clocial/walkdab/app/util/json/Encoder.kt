package com.clocial.walkdab.app.util.json

import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.collections.List
import kotlin.collections.Map

open class Encoder {
    var buf: StringBuilder = StringBuilder()

    // Keep track of circular data-structures: before encoding a
    // JSON-Object/Hash/Map/List/Array make sure it's not contained in
    // `circ`. If it is contained, throw an exception, b/c we can't encode
    // circular structs. If it's not contained, put it in so that we can
    // recognize it next time around...
    //
    // A `Set` would be a better fit here but:
    //   * HashSet's get confused at circular Maps
    //   * TreeSet's won't work w/out a custom Comparator
    //   * I got sick of fiddling around with this crap.
    private var circ: MutableList<Any> = mutableListOf()

    /**
     * override this in subclasses to allow custom encoding
     */
    open fun canEncode(o: Any): Boolean {
        return true
    }

    open fun encodeCustom(o: Any) {
        eggsplod("unexpected object: " + o.javaClass)
    }

    fun encode(o: Any?) {
        if (null == o) {
            buf.append("null")
            return
        }
        if (o is Map<*, *>) {
            encode(o)
        } else if (o is List<*>) {
            encode(o)
        } else if (o is Number) {
            encode(buf, o)
        } else if (o is CharSequence) {
            encode(buf, o)
        } else if (o is Char) {
            encode(buf, o)
        } else if (o is Boolean) {
            encode(buf, o)
        } else if (o is LocalDate) {
            encode(buf, o)
        } else if (o is LocalDateTime) {
            encode(buf, o)
        } else if (o.javaClass.isArray) {
            encodeArray(o as Array<Any>)
        } else {
            if (canEncode(o)) {
                encodeCustom(o)
            } else {
                eggsplod(o.javaClass)
            }
        }
    }

    fun eggsplod(o: Any) {
        throw RuntimeException("JSON encoder cannot handle class " + o.toString())
    }

    fun encode(m: Map<*, *>) {
        checkCircular(m)
        buf.append('{')
        for (k in m.keys) {
            val v = m[k]
            encode(buf, k.toString())
            buf.append(':')
            encode(v)
            buf.append(",")
        }
        buf.setCharAt(buf.length - 1, '}')
    }

    fun encode(l: List<*>) {
        checkCircular(l)
        buf.append('[')
        for (k in l) {
            encode(k)
            buf.append(",")
        }
        buf.setCharAt(buf.length - 1, ']')
    }

    fun encodeArray(arr: Array<Any>) {
        checkCircular(arr)
        buf.append('[')
        val l = arr.size
        var i = 0
        while (i < l) {
            encode(arr[i++])
            buf.append(",")
        }
        buf.setCharAt(buf.length - 1, ']')
    }

    fun checkCircular(m: Any) {
        if (circ.contains(m)) {
            eggsplod("circular")
        } else {
            circ.add(m)
        }
    }

    companion object {
        fun encode(buf: StringBuilder, s: CharSequence) {
            var c: Char
            val t = 12
            val ff = t.toChar()
            buf.append('"')
            for (element in s) {
                c = element
                if (Character.isISOControl(c)) {
                    continue  // really!? just skip?
                }
                when (c) {
                    '"', '\\', '\b', ff, '\n', '\r', '\t' -> {
                        buf.append('\\')
                        buf.append(c)
                        continue
                    }

                    else -> buf.append(c)
                }
            }
            buf.append('"')
        }

        fun encode(buf: StringBuilder, n: Number) {
            buf.append(n.toString())
        }

        fun encode(buf: StringBuilder, d: LocalDateTime) {
            encode(buf, pad2(d.year) + "-" + pad2(d.monthValue) + "-" + pad2(d.dayOfMonth) + " " +
                pad2(d.hour) + ":" + pad2(d.minute) + ":" + pad2(d.second) + "." + pad2(d.nano))
        }

        fun encode(buf: StringBuilder, d: LocalDate) {
            encode(buf, pad2(d.year) + "-" + pad2(d.monthValue) + "-" + pad2(d.dayOfMonth))
        }

        private fun pad2(v: Int): String {
            return v.toString().padStart(2,'0')
        }

        fun encode(buf: StringBuilder, b: Boolean) {
            if (b) {
                buf.append("true")
            } else {
                buf.append("false")
            }
        }

        fun encode(buf: StringBuilder, i: Int) {
            buf.append(i)
        }

        fun encode(buf: StringBuilder, i: Long) {
            buf.append(i)
        }

        fun encode(buf: StringBuilder, i: Float) {
            buf.append(i)
        }

        fun encode(buf: StringBuilder, i: Double) {
            buf.append(i)
        }

        fun encode(buf: StringBuilder, i: Byte) {
            buf.append(i.toInt())
        }

        fun encode(buf: StringBuilder, i: Short) {
            buf.append(i.toInt())
        }

        fun encode(buf: StringBuilder, c: Char) {
            buf.append(c.code)
        }
    }
}
