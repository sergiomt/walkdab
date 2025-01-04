package com.clocial.walkdab.app.pojos

import java.io.Serializable
import java.lang.IllegalArgumentException

import com.clocial.walkdab.app.models.forms.Bookmark
import com.clocial.walkdab.app.models.forms.FieldValueBookmark

class FieldValueBookmarkPojo : FieldValueBookmark, Serializable {

    private var bookmark: BookmarkPojo

    constructor() {
        bookmark = BookmarkPojo()
    }
    constructor(title: String, uri: String) {
        bookmark = BookmarkPojo()
        bookmark.setTitle(title)
        bookmark.setUri(uri)
    }

    override fun setValue(value: Any?) {
        if (value is Bookmark) {
            bookmark = BookmarkPojo(value)
        } else if (value is String) {
            if (value.isEmpty()) {
                bookmark = BookmarkPojo()
            } else {
                bookmark = BookmarkPojo()
                setFromString(value as String?)
            }
        } else {
            throw ClassCastException("Cannot cast from " + value!!.javaClass.name + " to Bookmark")
        }
    }

    @Throws(IllegalArgumentException::class)
    override fun setFromString(strRepresentation: String?) {
        if (strRepresentation == null || strRepresentation.trim { it <= ' ' }.isEmpty()) {
            bookmark = BookmarkPojo()
        } else {
            val strLen = strRepresentation.length
            val leftBracket = strRepresentation.indexOf('[')
            require(-1 != leftBracket) { "Missing left bracket [ $strRepresentation" }
            val rightBracket = strRepresentation.indexOf(']', leftBracket)
            require(-1 != rightBracket) { "Missing right bracket ] $strRepresentation" }
            val leftParenthesis = strRepresentation.indexOf('(', rightBracket)
            require(-1 != leftParenthesis) { "Missing left parenthesis ( $strRepresentation" }
            val rightParenthesis = strRepresentation.indexOf(')', leftParenthesis)
            require(-1 != rightParenthesis) { "Missing right parenthesis ) $strRepresentation" }
            var leftIcon = -1
            for (c in rightParenthesis + 1 until strLen) {
                if (strRepresentation[c] != ' ' && strRepresentation[c] != '\t') {
                    leftIcon = c
                    break
                }
            }
            bookmark = BookmarkPojo()
            bookmark.setTitle(strRepresentation.substring(leftBracket + 1, rightBracket))
            bookmark.setUri(strRepresentation.substring(leftParenthesis + 1, rightParenthesis))
            if (leftIcon > 0) {
                bookmark.setIcon(strRepresentation.substring(leftIcon))
            }
        }
    }

    override fun toString(): String {
        return if (bookmark != null) bookmark.toString() else ""
    }

    override fun isSearchable(): Boolean {
        return true
    }

    override fun getValue(): Bookmark {
        return bookmark
    }

    override fun equals(other: Any?): Boolean {
        return if (other is FieldValueBookmark) {
            getValue() == other.getValue()
        } else if (other is Bookmark) {
            getValue() == other
        } else {
            false
        }
    }
}