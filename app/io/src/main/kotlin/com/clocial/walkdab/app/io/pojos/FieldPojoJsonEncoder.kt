package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.Field
import com.clocial.walkdab.app.pojos.FieldPojo
import com.clocial.walkdab.app.pojos.FieldValueBooleanPojo
import com.clocial.walkdab.app.pojos.FieldValueStringPojo
import com.clocial.walkdab.app.util.json.CustomEncoder

class FieldPojoJsonEncoder: JsonEncoderPojo(), CustomEncoder.Encoder<FieldPojo>  {

    override fun encode(builder: StringBuilder, r: Any?) {
        val f: Field = r as FieldPojo
        encodeFirstNameValue(builder, "fieldId", f.getId())
        encodeNextNameValue (builder, "fieldName", f.getId())
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
        val fldVal = nameValueMap.get("fieldValue")
        when (nameValueMap.get("fieldType")) {
            FieldValueBooleanPojo::class.simpleName -> fld.setValue(FieldValueBooleanPojo(fldVal as Boolean))
            FieldValueStringPojo::class.simpleName -> fld.setValue(FieldValueStringPojo(fldVal as String))
        }
        nameValueMap.keys.forEach {
            val keyVal = nameValueMap.get(it)
            when (it) {
                "fieldId" -> fld.setId(keyVal as String)
                "fieldName" -> keyVal?.let { it1 -> fld.setName(it1 as String) }
                "fieldOptional" -> keyVal?.let { it1 -> fld.setOptional(it1 as Boolean) }
                "fieldNullable" -> keyVal?.let { it1 -> fld.setNullable(it1 as Boolean) }
            }
        }
        return fld
    }

}