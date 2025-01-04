package com.clocial.walkdab.app.models.snippets

interface GloballyUnique : Identifiable {

    fun getKey(): String

    fun setKey(guid: String)
}