package com.clocial.walkdab.app.models.forms

open class FieldValueNull : FieldValue {
    override fun setValue(value: Any?) {
        // Do nothing
    }

    override fun setFromString(strRepresentation: String?) {
        // Do nothing
    }

    override fun getValue(): Any? {
        return null
    }
    override fun isSearchable(): Boolean {
        return false
    }

    fun setSearchable(searchable: Boolean) {
        // Do nothing
    }
}