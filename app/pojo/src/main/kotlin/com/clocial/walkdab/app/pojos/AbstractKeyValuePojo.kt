package com.clocial.walkdab.app.pojos

import java.io.Serializable

import com.clocial.walkdab.app.models.snippets.KeyValuePair

abstract class AbstractKeyValuePojo : KeyValuePair {

    private var buckName: String?

    private var keyStr: String

    private var storedValue: Serializable? = null

    constructor() {
        buckName = ""
        keyStr = ""
    }

    constructor(bucketName: String?) {
        buckName = bucketName
        keyStr = ""
    }

    constructor(bucketName: String?, key: String) {
        buckName = bucketName
        keyStr = key
    }

    final override fun getKey(): String {
        return keyStr
    }

    final override fun setKey(key: String) {
        keyStr = key
    }

    override fun getValue(): Serializable? {
        return storedValue
    }

    final override fun setValue(data: Serializable?) {
        storedValue = data
    }

    final override fun getBucketName(): String? {
        return buckName
    }

    final override fun setBucketName(bucketName: String?) {
        buckName = bucketName
    }
}