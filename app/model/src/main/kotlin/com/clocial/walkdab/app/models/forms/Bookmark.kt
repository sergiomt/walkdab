package com.clocial.walkdab.app.models.forms

interface Bookmark : Cloneable {
    fun getTitle(): String

    fun getUri(): String

    fun getIcon(): String?
}