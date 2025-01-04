package com.clocial.walkdab.app.models.forms

interface FieldValueBookmark : FieldValue {
    override fun getValue(): Bookmark?
}