package com.clocial.walkdab.app.models.forms

import java.time.LocalDate
import java.time.LocalDateTime

interface FormsFactory {

    fun createForm() : Form

    fun createFormSet() : FormSet

    fun createTab(): Tab

    fun createField(fieldName: String, fieldValue: Boolean?): Field

    fun createField(fieldName: String, fieldValue: String?): Field

    fun createField(fieldName: String, fieldValue: LocalDate?): Field

    fun createField(fieldName: String, fieldValue: String, title: String): Field
    fun createField(fieldName: String, fieldValue: String, expiration: LocalDateTime?): Field
    fun createField(fieldName: String, fieldValue: ByteArray): Field
}