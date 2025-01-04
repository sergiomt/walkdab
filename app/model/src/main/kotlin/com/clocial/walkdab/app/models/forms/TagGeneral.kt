package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.enums.locale.ISO639_1LanguageCode

interface TagGeneral : TagCanonical, Comparable<TagGeneral> {

    fun getCanonicalGuid(): String

    fun getLanguageCode(): ISO639_1LanguageCode?
}