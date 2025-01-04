package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.Identifiable
import com.clocial.walkdab.app.models.snippets.Named
import java.io.Serializable

interface Field : Auditable, Identifiable, Named, Serializable, FieldSemantic {
    fun getValue(): FieldValue?

    fun isOptional(): Boolean

    fun isNullable(): Boolean

    fun setId(id: String?)
}