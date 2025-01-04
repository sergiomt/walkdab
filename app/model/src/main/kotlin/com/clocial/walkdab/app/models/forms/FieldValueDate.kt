package com.clocial.walkdab.app.models.forms

import java.time.LocalDate

interface FieldValueDate : FieldValue {

    override fun getValue(): LocalDate?

}