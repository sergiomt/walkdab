package com.clocial.walkdab.app.pojos

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import com.clocial.walkdab.app.encoding.BaseA64
import com.clocial.walkdab.app.models.forms.FieldValuePassword

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
        txPasswrd = if (null == value) "" else if (value is String) value else value.toString()
    }

    override fun setFromString(strRepresentation: String?) {
        if (strRepresentation.isNullOrEmpty()) {
            txPasswrd = ""
        } else {
            val comma = strRepresentation.indexOf(';')
            expiration = if (comma > 0) parseCompactTimestamp(strRepresentation.substring(comma)) else null
            val encodedPsswrd = if (comma > 0) strRepresentation.substring(0, comma) else strRepresentation
            txPasswrd = String(BaseA64.decode(encodedPsswrd.toCharArray()), Charsets.UTF_8)
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
        val encodedPsswrd = BaseA64.encode(txPasswrd.toByteArray(Charsets.UTF_8)).contentToString()
        return if (expiration==null) encodedPsswrd else encodedPsswrd + ";" + formatCompactTimestamp(expiration)
    }

    private fun formatCompactTimestamp(dt: LocalDateTime?): String {
        val ts = dt ?: LocalDateTime.now()
        val sb = StringBuilder(14)
        sb.append(ts.year.toString())
        sb.append(ts.monthValue.toString())
        sb.append(ts.dayOfMonth.toString())
        sb.append(ts.hour.toString())
        sb.append(ts.minute.toString())
        sb.append(ts.second.toString())
        return sb.toString()
    }

    private fun parseCompactTimestamp(st: String): LocalDateTime {
        return LocalDateTime.of(st.substring(0,4).toInt(), st.substring(4,6).toInt(), st.substring(6,8).toInt(),
            st.substring(8,10).toInt(), st.substring(10,12).toInt(), st.substring(12,14).toInt())
    }

}