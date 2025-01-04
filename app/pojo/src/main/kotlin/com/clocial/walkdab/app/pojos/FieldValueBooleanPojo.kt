package com.clocial.walkdab.app.pojos

import java.io.Serializable

import com.clocial.walkdab.app.models.forms.FieldValueBoolean

class FieldValueBooleanPojo : FieldValueBoolean, Serializable {

    private var boolValue: Boolean?

    constructor() {
        boolValue = null
    }

    constructor(value: Boolean?) {
        boolValue = value
    }

    constructor(strValue: String?) {
        boolValue = if (strValue.isNullOrEmpty()) java.lang.Boolean.parseBoolean(
            strValue
        ) else null
    }

    override fun getValue(): Boolean? {
        return boolValue
    }

    override fun setValue(value: Any?) {
        if (null == value)
            boolValue = null
        else if (value is Boolean)
            boolValue = value
        else if (value is String)
            boolValue = value.toBoolean()
        else
            throw IllegalArgumentException("Unsupported conversion from " + value.javaClass.name + " to Boolean")
    }

    override fun setFromString(strRepresentation: String?) {
        boolValue = if (strRepresentation.isNullOrEmpty()) java.lang.Boolean.parseBoolean(
            strRepresentation
        ) else null
    }

    override fun isSearchable(): Boolean {
        return true
    }

    override fun hashCode(): Int {
        return if (boolValue != null) boolValue.hashCode() else Boolean.hashCode()
    }

    override fun toString(): String {
        return if (boolValue != null) boolValue.toString() else ""
    }

    private fun nullSafeEqBool(a: Boolean?, b: Boolean?): Boolean = if (a == null && b == null) true else if (a == null && b != null) false else if (a != null && b == null) false else a == b

    override fun equals(obj: Any?): Boolean {
        return if (obj is FieldValueBoolean) {
            nullSafeEqBool(getValue(), obj.getValue())
        } else if (obj is Boolean) {
            nullSafeEqBool(getValue(), obj as Boolean?)
        } else if (obj is String) {
            nullSafeEqBool(getValue(), obj.toBoolean())
        } else {
            false
        }
    }
}