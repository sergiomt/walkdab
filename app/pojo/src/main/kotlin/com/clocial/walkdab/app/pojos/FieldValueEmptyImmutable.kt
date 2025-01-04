package com.clocial.walkdab.app.pojos

import java.io.Serializable

import com.clocial.walkdab.app.models.forms.FieldValueString

class FieldValueEmptyImmutable : FieldValueString, Serializable {

    override fun getValue(): String? {
        return ""
    }

    override fun setValue(value: Any?) {
        throw UnsupportedOperationException("Immutable fields do not allow modification of their values")
    }

    override fun setFromString(strRepresentation: String?) {
        throw UnsupportedOperationException("Immutable fields do not allow modification of their values")
    }

    override fun toString(): String {
        return "false;"
    }

    override fun isSearchable(): Boolean {
        return false
    }

    override fun setSearchable(searchable: Boolean) {
        throw UnsupportedOperationException("Immutable fields do not allow modification of their attributes")
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is FieldValueString && "".equals(other.getValue()) || other is String && "".equals(other)
    }
}