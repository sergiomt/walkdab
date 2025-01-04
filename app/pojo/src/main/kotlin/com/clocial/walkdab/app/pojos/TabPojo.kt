package com.clocial.walkdab.app.pojos

import java.time.LocalDateTime

import com.clocial.walkdab.app.models.forms.Field
import com.clocial.walkdab.app.models.forms.Tab

open class TabPojo : AbstractAuditablePojo, Tab {

    private var tabId: String
    private var tabName: String
    private var frmId: String
    private var tabFields: MutableList<Field>

    constructor() {
        tabId = ""
        tabName = ""
        frmId = ""
        tabFields = mutableListOf()
        setCreatedOn(LocalDateTime.now())
        setCreatedBy("")
    }

    constructor(frmGuid: String) {
        tabId = ""
        tabName = ""
        frmId = frmGuid
        tabFields = mutableListOf()
        setCreatedOn(LocalDateTime.now())
    }

    override fun getFormId(): String {
        return frmId
    }

    override fun setFormId(formId: String) {
        frmId = formId
    }

    override fun getFields(): List<Field> {
        return tabFields
    }

    override fun add(field: Field): Boolean {
        if (field.getId().isEmpty())
            return addWithNextId(field)
        else
            return tabFields.add(field)
    }

    override fun addOrReplace(field: Field): Int {
        var index = tabFields.indexOf(field)
        if (index >= 0) {
            tabFields[index] = field
        } else {
            tabFields.add(field)
            index = tabFields.size - 1
        }
        return index
    }

    override fun remove(fieldPosition: Int) {
        tabFields.removeAt(fieldPosition)
    }

    override fun remove(field: Field) {
        tabFields.remove(field)
    }

    override fun getId(): String {
        return tabId
    }

    fun setId(id: String) {
        tabId = id
    }

    override fun getName(): String {
        return tabName
    }

    override fun setName(name: String) {
        tabName = name
    }

    private fun addWithNextId(field: Field): Boolean {
        return tabFields.add(withNextId(field))
    }

    private fun withNextId(field: Field): Field {
        field.setId(String.format("%1$7s", (tabFields.size + 1).toString()).replace(' ', '0'))
        return field
    }

}