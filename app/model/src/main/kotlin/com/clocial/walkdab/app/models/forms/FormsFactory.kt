package com.clocial.walkdab.app.models.forms

import java.time.LocalDate

interface FormsFactory {

    fun createForm() : Form

    fun createFormSet() : FormSet

    fun createTab(): Tab

    fun createField(fieldName: String,  fieldValue: Boolean?): Field

    fun createField(fieldName: String,  fieldValue: String?): Field

    fun createField(fieldName: String,  fieldValue: LocalDate?): Field
}