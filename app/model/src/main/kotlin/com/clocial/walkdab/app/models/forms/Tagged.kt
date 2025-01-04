package com.clocial.walkdab.app.models.forms

interface Tagged {

    fun getGeneralTags(): Set<String>

    fun containsGeneralTag(tagGuid: String): Boolean
}