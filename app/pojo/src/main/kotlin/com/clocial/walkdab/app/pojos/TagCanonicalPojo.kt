package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.forms.TagCanonical

open class TagCanonicalPojo : AbstractAuditableGuidPojo(), TagCanonical {

    private var tagTenantId: String = ""

    private var tagText: String = ""

    override fun hashCode(): Int {
        return (getId() + getTenantId() + getText()).hashCode()
    }

    override fun getId(): String {
        return getKey()
    }

    fun setId(id: String) {
        setKey(id)
    }

    override fun getTenantId(): String {
        return tagTenantId
    }

    fun setTenantId(tenantId: String) {
        tagTenantId = tenantId
    }

    override fun getText(): String {
        return tagText
    }

    override fun setText(txt: String) {
        tagText = txt
    }

    override fun equals(other: Any?): Boolean {
        return other is TagCanonical && getId().equals(other.getId()) && getTenantId().equals(other.getTenantId()) && getText().equals(other.getText())
    }

}