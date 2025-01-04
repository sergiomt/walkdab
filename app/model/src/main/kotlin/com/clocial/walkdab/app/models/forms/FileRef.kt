package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.GloballyUnique
import com.clocial.walkdab.app.models.snippets.Identifiable
import java.io.Serializable
import java.net.URI

interface FileRef : Auditable, GloballyUnique, Identifiable, Serializable {

    fun getUri(): URI

    fun getTitle(): String

    fun getSize(): Long?

    fun getAuthor(): String?
}