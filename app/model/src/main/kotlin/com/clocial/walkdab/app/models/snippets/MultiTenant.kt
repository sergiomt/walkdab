package com.clocial.walkdab.app.models.snippets

interface MultiTenant {
    fun getTenantId(): String
}