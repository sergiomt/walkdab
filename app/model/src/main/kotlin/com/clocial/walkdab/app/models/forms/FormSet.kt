package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.GloballyUnique
import com.clocial.walkdab.app.models.snippets.MultiTenant
import java.io.Serializable

interface FormSet : Auditable, GloballyUnique, MultiTenant, Serializable {

    fun getName(): String

    fun getForms(): List<Form>
}