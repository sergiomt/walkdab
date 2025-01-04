package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.forms.Field
import java.io.Serializable
import java.time.LocalDateTime

import com.clocial.walkdab.app.models.forms.FieldValue

open class FieldPojo : FieldSemanticPojo(), Field {

    private var fieldId: String?
    private var fieldName: String
    private var fieldMandatory: Boolean
    private var fieldNullable: Boolean

    init {
        fieldId = null
        fieldName = ""
        fieldMandatory = false
        fieldNullable = true
        setCreatedOn(LocalDateTime.now())
        setCreatedBy("")
        setValue(EMPTY_FIELD)
    }

    override fun getValue(): FieldValue {
        return super.getValue() as FieldValue
    }

    fun setValue(fieldValue: FieldValue) {
        super.setValue(fieldValue)
    }

    fun setValue(value: String?) {
        super.setValue(FieldValueStringPojo(value))
    }

    override fun isOptional(): Boolean {
        return !fieldMandatory
    }

    fun setOptional(optionable: Boolean) {
        fieldMandatory = !optionable
    }

    override fun isNullable(): Boolean {
        return fieldNullable
    }

    fun setNullable(nullable: Boolean) {
        fieldNullable = nullable
    }

    override fun getId(): String {
        return fieldId ?: ""
    }

    override fun setId(id: String?) {
        fieldId = id
    }

    override fun getName(): String {
        return fieldName
    }

    override fun setName(objName: String) {
        fieldName = objName
    }

    companion object {
        val EMPTY_FIELD = FieldValueEmptyImmutable()
    }

}