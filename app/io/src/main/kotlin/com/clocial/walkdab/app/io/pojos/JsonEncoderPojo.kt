package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.*
import com.clocial.walkdab.app.models.snippets.Signature
import com.clocial.walkdab.app.util.json.Encoder
import com.clocial.walkdab.app.util.json.NullValue
import com.clocial.walkdab.app.util.time.TimeHelper.formatCompactDate
import com.clocial.walkdab.app.util.time.TimeHelper.formatCompactTimestamp
import com.clocial.walkdab.app.pojos.EncodedData

import java.time.LocalDate
import java.time.LocalDateTime

abstract class JsonEncoderPojo {

    abstract fun decode(nameValueMap: Map<String, Any>): Any

    abstract fun encode(builder: StringBuilder, r: Any?)

    protected fun encodeFirstNameValue(builder: StringBuilder, fieldName: String, fieldValue: Any?) {
        builder.append("{")
        encodeNameValue(builder, fieldName, fieldValue)
        builder.append(",")
    }

    protected fun encodeListItems(builder: StringBuilder, fieldName: String, fieldValue: Any?) {
        builder.append("[")
        encodeNameValue(builder, fieldName, fieldValue)
        builder.append(",")
    }

    protected fun encodeNextNameValue(builder: StringBuilder, fieldName: String, fieldValue: Any?) {
        encodeNameValue(builder, fieldName, fieldValue)
        builder.append(",")
    }

    protected fun encodeLastNameValue(builder: StringBuilder, fieldName: String, fieldValue: Any?) {
        encodeNameValue(builder, fieldName, fieldValue)
        builder.append("}")
    }

    private fun encodeNameValue(builder: StringBuilder, fieldName: String, fieldValue: Any?) {
        if (fieldValue==null) {
            builder.append('"').append(fieldName).append('"').append(":null")
        } else if (fieldValue is FieldValue) {
            if (fieldValue is FieldValuePassword) {
                encodeNameValue(builder, fieldName, fieldValue.toString())
            } else {
                encodeNameValue(builder, fieldName, fieldValue.getValue())
            }
        } else {
            builder.append('"').append(fieldName).append('"').append(':')
            if (fieldValue is CharSequence) {
                if (fieldValue.length==0)
                    builder.append('"').append('"')
                else
                    Encoder.encode(builder, fieldValue)
                return
            }
            if (fieldValue is LocalDate) {
                Encoder.encode(builder, formatCompactDate(fieldValue))
                return
            }
            if (fieldValue is LocalDateTime) {
                Encoder.encode(builder, formatCompactTimestamp(fieldValue))
                return
            }
            if (fieldValue is Boolean) {
                Encoder.encode(builder, fieldValue)
                return
            }
            if (fieldValue is Bookmark) {
                Encoder.encode(builder, fieldValue.toString())
                return
            }
            if (fieldValue is Signature) {
                Encoder.encode(builder, fieldValue.toString())
                return
            }
            if (fieldValue is ByteArray) {
                Encoder.encode(builder, EncodedData(fieldValue).toString())
                return
            }
            if (fieldValue is Set<*>) {
                Encoder.encode(builder, fieldValue)
                return
            }
            if (fieldValue is Collection<*>) {
                if (fieldValue.all { i -> i is Field }) {
                    builder.append(encodeFields(fieldValue as Collection<Field>))
                } else if (fieldValue.all { i -> i is Tab }) {
                    builder.append(encodeTabs(fieldValue as Collection<Tab>))
                } else {
                    encode(builder, fieldValue)
                }
                return
            }
            throw RuntimeException("Unsupported field type " + fieldValue.javaClass.simpleName)
        }
    }

    private fun encodeFields(fieldValues: Collection<Field>): String {
        val fieldEncoder = FieldPojoJsonEncoder()
        val fieldBuilder = StringBuilder()
        val fieldJsons = mutableListOf<String>()
        for (f in fieldValues) {
            fieldEncoder.encode(fieldBuilder, f)
            fieldJsons.add(fieldBuilder.toString())
            fieldBuilder.setLength(0)
        }
        return "[" + fieldJsons.joinToString() + "]"
    }

    private fun encodeTabs(tabValues: Collection<Tab>): String {
        val fieldEncoder = TabPojoJsonEncoder()
        val fieldBuilder = StringBuilder()
        val fieldJsons = mutableListOf<String>()
        for (t in tabValues) {
            fieldEncoder.encode(fieldBuilder, t)
            fieldJsons.add(fieldBuilder.toString())
            fieldBuilder.setLength(0)
        }
        return "[" + fieldJsons.joinToString() + "]"
    }

    protected fun nil(it: Any?): Any? {
        if (null==it)
            return null
        else if (it is NullValue)
            return null
        else
            return it
    }
}