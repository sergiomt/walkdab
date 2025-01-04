package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.forms.Bookmark
import com.clocial.walkdab.app.util.str.Str.nullSafeEq

class BookmarkPojo : Bookmark {

    private var titleStr: String

    private var uriStr: String

    private var iconStr: String?

    constructor() {
        titleStr = ""
        uriStr = ""
        iconStr = null
    }

    constructor(bmrk: Bookmark) {
        titleStr = bmrk.getTitle()
        uriStr = bmrk.getUri()
        iconStr = bmrk.getIcon()
    }

    override fun clone(): Bookmark {
        val theClone = BookmarkPojo()
        theClone.titleStr = getTitle()
        theClone.uriStr = getUri()
        theClone.iconStr = getIcon()
        return theClone
    }

    override fun getTitle(): String {
        return titleStr
    }

    fun setTitle(title: String) {
        titleStr = title
    }

    override fun getUri(): String {
        return uriStr
    }

    fun setUri(uri: String) {
        uriStr = uri
    }

    override fun getIcon(): String? {
        return iconStr
    }

    fun setIcon(icon: String?) {
        iconStr = icon
    }

    override fun toString(): String {
        return "[" + getTitle() + "](" + getUri() + ")"
    }

    override fun equals(other: Any?): Boolean {
        return other is BookmarkPojo && nullSafeEq(getTitle(), other.getTitle()) && nullSafeEq(getUri(), other.getUri())

    }
}