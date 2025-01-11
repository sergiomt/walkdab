package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.Field
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
        encodeNextNameValue (builder, "fieldType", f.javaClass.simpleName)
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
            FieldValuePasswordPojo::class.simpleName -> fld.setValue(fldVal as String)
            FieldValueBookmarkPojo::class.simpleName -> fld.setValue(fldVal as String)
        }
        nameValueMap.keys.forEach {
            val keyVal = nameValueMap[it]
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

}