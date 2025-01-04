package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.forms.Form
import com.clocial.walkdab.app.models.forms.Tab
import com.clocial.walkdab.app.models.snippets.Signature

open class FormPojo : AbstractAuditableDeletableGuidPojo(), Form {

    private var formTenantId: String
    private var formName: String
    private var formSignature: Signature? = null
    private var formTabs: MutableList<Tab>
    private val formTags: MutableSet<String>

    init {
        formTenantId = ""
        formName = ""
        formTabs = mutableListOf(TabPojo())
        formTags = mutableSetOf()
        formSignature = null
    }

    final override fun addTab(newTab: Tab) {
        formTabs.add(newTab)
    }

    final override fun removeTab(tabId: String) {
        var index = -1
        var i = 0
        for (t in formTabs) {
            if (t.getId() == tabId) {
                index = i
                break
            } else {
                i++
            }
        }
        if (-1 == index) {
            throw NoSuchElementException("No tab with id $tabId was found")
        }
        formTabs.removeAt(index)
    }

    final override fun getTabs(): List<Tab> {
        return formTabs
    }

    final override fun getFirstTab(): Tab {
        return formTabs[0]
    }

    final override fun getGeneralTags(): Set<String> {
        return formTags
    }

    final override fun addGeneralTag(tagGuid: String) {
        formTags.add(tagGuid)
    }

    final override fun removeGeneralTag(tagGuid: String) {
        formTags.remove(tagGuid)
    }

    final override fun containsGeneralTag(tagGuid: String): Boolean {
        return formTags.contains(tagGuid)
    }

    final override fun getId(): String {
        return getKey()
    }

    final override fun getTenantId(): String {
        return formTenantId
    }

    fun setTenantId(tenantId: String) {
        formTenantId = tenantId
    }

    final override fun getName(): String {
        return formName
    }

    override fun setName(name: String) {
        formName = name
    }

    final override fun getSignature(): Signature? {
        return formSignature
    }

    fun setSignature(sig: Signature?) {
        formSignature = sig
    }

}