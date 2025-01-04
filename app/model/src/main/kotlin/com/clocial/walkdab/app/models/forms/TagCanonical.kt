package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.GloballyUnique
import com.clocial.walkdab.app.models.snippets.MultiTenant

import java.io.Serializable

interface TagCanonical : Auditable, GloballyUnique, MultiTenant, Serializable {

    fun getText(): String

    fun setText(txt: String)

}