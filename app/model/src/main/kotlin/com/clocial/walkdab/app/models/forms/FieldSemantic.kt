package com.clocial.walkdab.app.models.forms

import java.io.Serializable

interface FieldSemantic : Serializable {

    fun getNameSpaceGuid(): String

    fun getMeaning(): String
}