package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.Field
import com.clocial.walkdab.app.util.json.Encoder
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
        builder.append('"').append(fieldName).append('"').append(':')
        if (fieldValue==null) {
            builder.append("null")
        } else {
            if (fieldValue is CharSequence) {
                if (fieldValue.length==0)
                    builder.append('"').append('"')
                else
                    builder.append(Encoder.encode(builder, fieldValue))
                return
            }
            if (fieldValue is LocalDateTime) {
                builder.append(Encoder.encode(builder, fieldValue))
                return
            }
            if (fieldValue is Boolean) {
                builder.append(Encoder.encode(builder, fieldValue))
                return
            }
            if (fieldValue is List<*>) {
                builder.append(Encoder.encode(builder, fieldValue))
                return
            }
            throw RuntimeException("Unsupported field type " + fieldValue.javaClass.simpleName)
        }
    }

}