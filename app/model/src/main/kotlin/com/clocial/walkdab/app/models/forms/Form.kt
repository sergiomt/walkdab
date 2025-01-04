package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.GloballyUnique
import com.clocial.walkdab.app.models.snippets.Deletable
import com.clocial.walkdab.app.models.snippets.MultiTenant
import com.clocial.walkdab.app.models.snippets.Named
import com.clocial.walkdab.app.models.snippets.Signed

import java.io.Serializable

interface Form : Auditable, Deletable, GloballyUnique, MultiTenant, Named, Signed, Serializable, Tagged {

    fun getTabs(): List<Tab>

    fun addTab(newTab: Tab)

    fun getFirstTab(): Tab

    fun removeTab(tabId: String)

    fun addGeneralTag(tagGuid: String)

    fun removeGeneralTag(tagGuid: String)

    override fun containsGeneralTag(tagGuid: String): Boolean
}