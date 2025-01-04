package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.models.snippets.GloballyUnique
import java.io.Serializable

import kotlin.collections.Collection
import kotlin.collections.Set

interface Envelope : GloballyUnique, Serializable, Tagged {

    fun getCypher(): String

    fun getValue(): ByteArray

    fun getWrappedClassName(): String

    fun getKeywords(): Set<String>

    fun addKeyword(keyword: String)

    fun addKeywords(keywords: Collection<String>)

    companion object {
        const val NO_CYPHER = "NONE"
        const val SELF_SHA256 = "SHA256"
    }
}