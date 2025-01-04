package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.enums.locale.ISO639_1LanguageCode
import com.clocial.walkdab.app.models.forms.TagGeneral

open class TagGeneralPojo : TagCanonicalPojo, TagGeneral {

    private var canonicalTagGuid: String
    private var generalTagText: String?
    private var languageCode: ISO639_1LanguageCode?

    constructor(tagGuid: String) {
        canonicalTagGuid = tagGuid
        generalTagText = null
        languageCode = null
    }

    constructor(tagGuid: String, generalText: String) {
        canonicalTagGuid = tagGuid
        generalTagText = generalText
        languageCode = null
    }

    constructor(tagGuid: String, tagText: String, language: ISO639_1LanguageCode) {
        canonicalTagGuid = tagGuid
        generalTagText = tagText
        languageCode = language
    }

    override fun getCanonicalGuid(): String {
        return canonicalTagGuid
    }

    override fun getLanguageCode(): ISO639_1LanguageCode? {
        return languageCode
    }

    fun setLanguageCode(langCode: ISO639_1LanguageCode?) {
        languageCode = langCode
    }

    fun setCanonicalGuid(tagGuid: String) {
        canonicalTagGuid = tagGuid
    }

    override fun hashCode(): Int {
        return if (generalTagText!=null) generalTagText.hashCode() else "".hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is TagGeneral && getText().equals(other.getText())
    }

    override operator fun compareTo(other: TagGeneral): Int {
        var result = -1
        if (null != other) {
            val thisText = generalTagText
            val otherText = other.getText()
            if (null == thisText && null == otherText) {
                result = 0
            } else if (null == thisText || null != otherText) {
                result = thisText?.compareTo(otherText) ?: 1
            }
        }
        return result
    }

}