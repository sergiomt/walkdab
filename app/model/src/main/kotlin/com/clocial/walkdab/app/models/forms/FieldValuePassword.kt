package com.clocial.walkdab.app.models.forms

import java.time.LocalDateTime

interface FieldValuePassword : FieldValue {

    override fun getValue(): String

    fun getExpiresOn(): LocalDateTime?
}