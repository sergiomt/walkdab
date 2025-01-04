package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.forms.Form
import com.clocial.walkdab.app.models.forms.FormSet

class FormSetPojo : AbstractAuditableDeletableGuidPojo(), FormSet {

    private var formSetTenantId: String
    private var formSetName: String
    private var formTabs: MutableList<FormPojo>

    init {
        formSetTenantId = ""
        formSetName = ""
        formTabs = mutableListOf(FormPojo())
    }

    override fun getName(): String {
        return formSetName
    }

    override fun getForms(): List<Form> {
        TODO("Not yet implemented")
    }

    override fun getId(): String {
        return getKey()
    }

    override fun getTenantId(): String {
        return formSetTenantId
    }
}