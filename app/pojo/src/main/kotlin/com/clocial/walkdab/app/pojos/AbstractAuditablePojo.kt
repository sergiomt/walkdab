package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.snippets.Auditable

import java.time.LocalDateTime

abstract class AbstractAuditablePojo : Auditable, AbstractKeyValuePojo() {

    protected var createdOnDateTime: LocalDateTime? = null

    protected var createdByUser: String? = null

    protected var updatedOnDateTime: LocalDateTime? = null

    protected var updatedByUser: String? = null

    override fun getCreatedOn(): LocalDateTime? = createdOnDateTime

    override fun getCreatedBy(): String? = createdByUser

    override fun getUpdatedOn(): LocalDateTime? = updatedOnDateTime

    override fun getUpdatedBy(): String? = updatedByUser

    fun setCreatedOn(createdOn: LocalDateTime?) { createdOnDateTime = createdOn }

    fun setCreatedBy(createdBy: String?) { createdByUser = createdBy }

    fun setUpdatedOn(updatedOn: LocalDateTime?) { updatedOnDateTime = updatedOn }

    fun setUpdatedBy(updatedBy: String?) { updatedByUser = updatedBy }

}