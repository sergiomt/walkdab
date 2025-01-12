package com.clocial.walkdab.app.pojos

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import com.clocial.walkdab.app.encoding.BaseA64
import com.clocial.walkdab.app.models.forms.FieldValuePassword
import com.clocial.walkdab.app.util.time.TimeHelper.parseCompactTimestamp
import com.clocial.walkdab.app.util.time.TimeHelper.formatCompactTimestamp

class FieldValuePasswordPojo : FieldValuePassword, Serializable {

    private var txPasswrd: String
    private var expiration: LocalDateTime? = null

    constructor() {
        txPasswrd = ""
        expiration = null
    }
    constructor(psswrd: String) {
        this.txPasswrd = psswrd
    }

    constructor(psswrd: String, expiresOn: LocalDateTime?) {
        txPasswrd = psswrd
        expiration = expiresOn
    }

    constructor(psswrd: String, expiresOn: LocalDate) {
        txPasswrd = psswrd
        expiration = LocalDateTime.of(expiresOn, LocalTime.of(23,59,59))
    }

    override fun setValue(value: Any?) {
        if (value == null) {
            txPasswrd = ""
            expiration = null
        } else {
            setFromString(value.toString())
        }
    }

    override fun setFromString(strRepresentation: String?) {
        if (strRepresentation.isNullOrEmpty()) {
            txPasswrd = ""
            expiration = null
        } else {
            val comma = strRepresentation.indexOf(';')
            if (comma==0) {
                txPasswrd = ""
                expiration = if (strRepresentation.length > 1) parseCompactTimestamp(strRepresentation.substring(1)) else null
            } else {
                expiration = if (comma > 0 && comma < strRepresentation.length - 1) parseCompactTimestamp(strRepresentation.substring(comma + 1)) else null
                val encodedPsswrd = if (comma > 0) strRepresentation.substring(0, comma) else strRepresentation
                txPasswrd = String(BaseA64.decode(encodedPsswrd.toCharArray()), Charsets.UTF_8)
            }
        }
    }

    override fun isSearchable(): Boolean {
        return false
    }

    override fun getValue(): String {
        return txPasswrd
    }

    override fun getExpiresOn(): LocalDateTime? {
        return expiration
    }

    fun setExpiresOn(expires: LocalDateTime?) {
        expiration = expires
    }

    override fun toString(): String {
        if (txPasswrd.isEmpty()) {
            return ""
        }
        val encodedPsswrd = String(BaseA64.encode(txPasswrd.toByteArray(Charsets.UTF_8)))
        return if (expiration==null) encodedPsswrd else encodedPsswrd + ";" + formatCompactTimestamp(expiration)
    }



}