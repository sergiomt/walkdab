package com.clocial.walkdab.app.models.snippets

import java.io.Serializable

interface KeyValuePair {

    fun getKey(): String

    fun setKey(key: String)

    fun getValue(): Serializable?

    fun setValue(data: Serializable?)

    fun getBucketName(): String?

    fun setBucketName(bucketName: String?)
}