package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.Field
import com.clocial.walkdab.app.models.forms.FieldValue
import com.clocial.walkdab.app.models.forms.FieldValueNull
import com.clocial.walkdab.app.models.forms.FieldValuePassword
import com.clocial.walkdab.app.pojos.*
import com.clocial.walkdab.app.util.json.CustomEncoder
import com.clocial.walkdab.app.util.time.TimeHelper.parseCompactTimestamp

class FieldPojoJsonEncoder: JsonEncoderPojo(), CustomEncoder.Encoder<FieldPojo> {

    override fun encode(builder: StringBuilder, r: Any?) {
        val f: Field = r as FieldPojo
        encodeFirstNameValue(builder, "fieldId", f.getId())
        encodeNextNameValue (builder, "fieldName", f.getName())
        encodeNextNameValue (builder, "fieldOptional", f.isOptional())
        encodeNextNameValue (builder, "fieldNullable", f.isNullable())
        if (null!=f.getValue()) {
            encodeNextNameValue (builder, "fieldType", f.getValue()!!.javaClass.simpleName)
        } else {
            encodeNextNameValue (builder, "fieldType", FieldValueStringPojo::class.simpleName)
        }
        encodeNextNameValue (builder, "fieldValue", f.getValue())
        encodeNextNameValue (builder, "createdOn", f.getCreatedOn())
        encodeNextNameValue (builder, "createdBy", f.getCreatedBy())
        encodeNextNameValue (builder, "updatedOn", f.getUpdatedOn())
        encodeLastNameValue (builder, "updatedBy", f.getUpdatedBy())
    }

    override fun decode(nameValueMap: Map<String, Any>): FieldPojo {
        val fld = FieldPojo()
        val fldVal = nameValueMap["fieldValue"]
        when (nameValueMap["fieldType"]) {
            FieldValueBooleanPojo::class.simpleName -> fld.setValue(FieldValueBooleanPojo(fldVal as Boolean))
            FieldValueStringPojo::class.simpleName -> fld.setValue(FieldValueStringPojo(fldVal as String))
            FieldValueDatePojo::class.simpleName -> fld.setValue(FieldValueDatePojo(fldVal as String))
            FieldValuePasswordPojo::class.simpleName -> fld.setValue(getFieldValuePassword(fldVal as String))
            FieldValueBookmarkPojo::class.simpleName -> fld.setValue(getFieldValueBookmark(fldVal as String))
            FieldValueBytesPojo::class.simpleName -> fld.setValue(fldVal as String)
            FieldValueNullPojo::class.simpleName -> fld.setValue(null)
        }
        nameValueMap.keys.forEach {
            var keyVal = nil(nameValueMap[it])
            when (it) {
                "fieldId" -> fld.setId(keyVal as String)
                "fieldName" -> keyVal?.let { it1 -> fld.setName(it1 as String) }
                "fieldOptional" -> keyVal?.let { it1 -> fld.setOptional(it1 as Boolean) }
                "fieldNullable" -> keyVal?.let { it1 -> fld.setNullable(it1 as Boolean) }
                "createdOn" -> keyVal?.let { it1 -> fld.setCreatedOn(parseCompactTimestamp(it1 as String)) }
                "createdBy" -> keyVal?.let { it1 -> fld.setCreatedBy(it1 as String) }
                "updatedOn" -> keyVal?.let { it1 -> fld.setCreatedOn(parseCompactTimestamp(it1 as String)) }
                "updatedBy" -> keyVal?.let { it1 -> fld.setUpdatedBy(it1 as String) }
            }
        }
        return fld
    }

    private fun getFieldValueBookmark(strRepresentation: String): FieldValue {
        val bookmarkVal = FieldValueBookmarkPojo()
        bookmarkVal.setFromString(strRepresentation)
        return bookmarkVal
    }
    private fun getFieldValuePassword(strRepresentation: String): FieldValue {
        val psswrdVal = FieldValuePasswordPojo()
        psswrdVal.setFromString(strRepresentation)
        return psswrdVal
    }
}