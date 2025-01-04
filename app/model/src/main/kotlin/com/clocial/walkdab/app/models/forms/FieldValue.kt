package com.clocial.walkdab.app.models.forms

import java.io.Serializable
import kotlin.Throws


interface FieldValue : Serializable {

    fun setValue(value: Any?)

    fun setFromString(strRepresentation: String?)

    fun getValue(): Any?

    fun isSearchable(): Boolean
}