package com.clocial.walkdab.app.pojos

import java.io.Serializable

import com.clocial.walkdab.app.models.forms.Envelope
import com.clocial.walkdab.app.models.snippets.KeyValuePair

open class EnvelopePojo : Envelope, KeyValuePair {

    private var cyphering: String

    private var bucket: String?

    private var envelopeId: String

    private val keywrds: MutableSet<String>

    private val tags: MutableSet<String>

    private var data: ByteArray

    private var wrappedClassName: String?

    constructor() {
        cyphering = Envelope.SELF_SHA256
        tags = mutableSetOf()
        keywrds = mutableSetOf()
        bucket = null
        envelopeId = ""
        data = byteArrayOf()
        wrappedClassName = null
    }

    constructor(cypher: String) {
        requireNotNull(cypher) { "Cypher argument cannot be null" }
        cyphering = cypher
        tags = mutableSetOf()
        keywrds = mutableSetOf()
        bucket = null
        envelopeId = ""
        data = byteArrayOf()
        wrappedClassName = null
    }

    override fun getCypher(): String {
        return cyphering
    }

    fun setCypher(cypher: String) {
        cyphering = cypher
    }

    override fun getValue(): ByteArray {
        return data
    }

    override fun getWrappedClassName(): String {
        TODO("Not yet implemented")
    }

    override fun getBucketName(): String? {
        return bucket
    }

    override fun setBucketName(bucketName: String?) {
        bucket = bucketName
    }

    override fun setValue(data: Serializable?) {
        this.data = data as ByteArray
    }

    override fun getKeywords(): MutableSet<String> {
        return keywrds
    }

    override fun addKeyword(keyWord: String) {
        keywrds.add(keyWord)
    }

    override fun addKeywords(keyWords: Collection<String>) {
        keywrds.addAll(keyWords)
    }

    override fun getKey(): String {
        return envelopeId
    }

    override fun setKey(guid: String) {
        envelopeId = guid
    }

    override fun getId(): String {
        return envelopeId
    }

    override fun getGeneralTags(): Set<String> { return tags }

    override fun containsGeneralTag(tagGuid: String): Boolean {
        return tags.contains(tagGuid)
    }

    fun addGeneralTag(tagGuid: String) {
        tags.add(tagGuid)
    }

    fun removeGeneralTag(tagGuid: String) {
        tags.remove(tagGuid)
    }
}