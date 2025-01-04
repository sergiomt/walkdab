package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.snippets.Deletable


abstract class AbstractAuditableDeletableGuidPojo : AbstractAuditableGuidPojo, Deletable {

    private var deleted: Boolean

    protected constructor() : super() {
        deleted = false
    }

    override fun isDeleted(): Boolean {
        return deleted
    }

    fun setDeleted(del: Boolean) {
        deleted = del
    }

}