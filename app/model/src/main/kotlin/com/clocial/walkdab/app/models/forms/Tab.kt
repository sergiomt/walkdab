package com.clocial.walkdab.app.models.forms

import kotlin.Throws

import java.io.Serializable

import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.Identifiable
import com.clocial.walkdab.app.models.snippets.Named

interface Tab : Auditable, Identifiable, Named, Serializable {

    fun getFormId(): String

    fun setFormId(id: String)

    fun getFields(): List<Field>

    fun getFieldByName(fieldName: String): Field?

    fun add(field: Field): Boolean

    fun addOrReplace(field: Field): Int

    fun remove(fieldPosition: Int)

    fun remove(field: Field)

}