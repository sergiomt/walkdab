package com.clocial.walkdab.app.models.forms

import kotlin.collections.List

interface FieldValueList : FieldValue {

    override fun getValue(): List<String>

    fun add(value: String)

    fun remove(value: String)

    fun remove(index: Int)

    fun clear()

    fun size(): Int
}