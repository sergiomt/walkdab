package com.clocial.walkdab.app.models.forms

interface FieldValueString : FieldValue {

    override fun getValue(): String?

    fun setSearchable(searchable: Boolean)
}