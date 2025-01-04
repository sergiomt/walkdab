package com.clocial.walkdab.app.pojos

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable

import com.clocial.walkdab.app.models.forms.FieldValueBytes
import com.clocial.walkdab.app.util.arrays.NullSafeArrays.arraysEquals

class FieldValueBytesPojo : FieldValueBytes, Serializable {

    private var encodedValue: EncodedData?

    constructor() {
        encodedValue = null
    }
    constructor(obj: Serializable?) {
        encodedValue = null
        setValue(obj)
    }

    override fun getValue(): ByteArray? {
        val encodedNonNull = encodedValue ?: EncodedData()
        return if (encodedNonNull.isEmpty()) null else encodedNonNull.toBytes()
    }

    override fun isSearchable(): Boolean {
        return false
    }

    override fun setValue(obj: Any?) {
        if (null == obj)
            encodedValue = null
        else if (obj is ByteArray)
            encodedValue = EncodedData(obj)
        else if (obj is EncodedData)
            encodedValue = obj
        else if (obj is String)
            encodedValue = EncodedData(obj.toByteArray(Charsets.UTF_8))
        else if (obj is ByteArrayOutputStream)
            encodedValue = EncodedData(obj.toByteArray())
        else if (obj is ByteArrayInputStream) {
                ByteArrayOutputStream().use { bout ->
                    pipe(obj, bout)
                    encodedValue = EncodedData(bout.toByteArray())
                }
        }
        else throw ClassCastException("Invalid class " + obj.javaClass.name)
    }

    override fun setFromString(strRepresentation: String?) {
        if (strRepresentation==null) encodedValue = null
        val semicolon = strRepresentation!!.indexOf(';')
        require(semicolon > 0) { strRepresentation }
        val base: Int
        base = try {
            strRepresentation.substring(0, semicolon).toInt()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException(strRepresentation, e)
        }
        encodedValue = EncodedData(base, strRepresentation.substring(semicolon + 1))
    }

    override fun toString(): String {
        val data = EncodedData(getValue())
        return data.base.toString() + ";" + data.toString()
    }

    override fun hashCode(): Int {
        return if (encodedValue!=null) encodedValue.hashCode() else "".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is FieldValueBytes &&
                (encodedValue == null && other.getValue() == null ||
                 encodedValue != null && getValue() != null && arraysEquals(getValue(), other.getValue()))
               ||
                other is ByteArray && getValue() != null && arraysEquals(getValue(), other as ByteArray?)
    }

    private fun pipe(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

}