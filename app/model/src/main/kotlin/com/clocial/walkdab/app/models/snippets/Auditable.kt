package com.clocial.walkdab.app.models.snippets

import java.time.LocalDateTime

interface Auditable {
    fun getCreatedOn(): LocalDateTime?
    fun getUpdatedOn(): LocalDateTime?
    fun getCreatedBy(): String?
    fun getUpdatedBy(): String?
}
