package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.models.snippets.*

import java.io.Serializable

interface Form : Auditable, Deletable, GloballyUnique, Identifiable, MultiTenant, Named, Signed, Serializable, Tagged {

    fun getTabs(): List<Tab>

    fun addTab(newTab: Tab)

    fun getFirstTab(): Tab

    fun removeTab(tabId: String)

    fun removeTab(tabIndex: Int)

    fun addGeneralTag(tagGuid: String)

    fun removeGeneralTag(tagGuid: String)

    override fun containsGeneralTag(tagGuid: String): Boolean
}