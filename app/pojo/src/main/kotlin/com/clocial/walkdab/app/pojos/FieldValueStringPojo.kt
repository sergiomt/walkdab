package com.clocial.walkdab.app.pojos

import java.io.Serializable

import com.clocial.walkdab.app.models.forms.FieldValueString

class FieldValueStringPojo : FieldValueString, Serializable {

    private var txValue: String?

    private var boSearchable: Boolean


    constructor() {
        txValue = null
        boSearchable = false
    }

    constructor(value: String?) {
        txValue = value
        boSearchable = false
    }

    constructor(value: String?, searchable: Boolean) {
        txValue = value
        boSearchable = searchable
    }

    override fun getValue(): String? {
        return txValue
    }

    override fun setValue(value: Any?) {
        txValue = if (null == value) null else if (value is String) value else value.toString()
    }

    override fun setFromString(strRepresentation: String?) {
        val comma = strRepresentation!!.indexOf(';')
        boSearchable = java.lang.Boolean.parseBoolean(strRepresentation.substring(0, comma))
        txValue = strRepresentation.substring(comma + 1)
    }

    override fun toString(): String {
        return boSearchable.toString() + ";" + if (txValue != null) txValue else ""
    }

    override fun isSearchable(): Boolean {
        return boSearchable
    }

    override fun setSearchable(searchable: Boolean) {
        boSearchable = searchable
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is FieldValueString &&
                (txValue == null && other.getValue() == null ||
                        txValue != null && txValue == other.getValue()) ||
                other is String && other == txValue
    }
}