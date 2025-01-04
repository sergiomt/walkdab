package com.clocial.walkdab.app.models.forms

interface FieldValueBytes : FieldValue {

    override fun getValue(): ByteArray?

}