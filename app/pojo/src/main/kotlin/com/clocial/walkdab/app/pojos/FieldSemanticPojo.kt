package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.forms.FieldSemantic


abstract class FieldSemanticPojo : AbstractAuditablePojo(), FieldSemantic {

    private var fieldNameSpaceGuid: String
    private var fieldMeaning: String

    init {
        fieldNameSpaceGuid = ""
        fieldMeaning = ""
    }

    override fun getNameSpaceGuid(): String {
        return fieldNameSpaceGuid
    }

    fun setNameSpaceGuid(nameSpaceGuid: String) {
        fieldNameSpaceGuid = nameSpaceGuid
    }

    override fun getMeaning(): String {
        return fieldMeaning
    }

    fun setMeaning(meaning: String) {
        fieldMeaning = meaning
    }

    override fun equals(other: Any?): Boolean {
        return other is FieldSemantic && fieldMeaning != null && fieldNameSpaceGuid != null && fieldMeaning == other.getMeaning() && fieldNameSpaceGuid == other.getNameSpaceGuid()
    }

    override fun hashCode(): Int {
        return ((if (fieldNameSpaceGuid != null) fieldNameSpaceGuid else "") + "." +
                if (fieldMeaning != null) fieldMeaning else "").hashCode()
    }


}