package com.clocial.walkdab.app.pojos

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

import com.clocial.walkdab.app.enums.locale.ISO3166CountryCode
import com.clocial.walkdab.app.enums.security.SignatureType
import com.clocial.walkdab.app.models.snippets.Signature
import com.clocial.walkdab.app.util.str.Str.ETX
import com.clocial.walkdab.app.util.str.Str.FF
import com.clocial.walkdab.app.util.str.Str.STX
import com.clocial.walkdab.app.util.str.Str.removeChars

class SignaturePojo : Signature {

    private var partyId: String? = null
    private var partyRole: String? = null
    private var authorityName: String? = null
    private var authorityOrganisation: String? = null
    private var authorityCountry: ISO3166CountryCode? = null
    private var timestamp: LocalDateTime? = null
    private var signatureType: SignatureType? = null
    private var signature: String? = null

    override fun getPartyId(): String? {
        return partyId
    }

    fun setPartyId(partyId: String?) {
        this.partyId = sanitize(partyId)
    }

    override fun getPartyRole(): String? {
        return partyRole
    }

    fun setPartyRole(partyRole: String?) {
        this.partyRole = sanitize(partyRole)
    }

    override fun getAuthorityCountry(): ISO3166CountryCode? {
        return authorityCountry
    }

    fun setAuthorityCountry(isoCountryCode: ISO3166CountryCode?) {
        authorityCountry = isoCountryCode
    }

    override fun getAuthorityName(): String? {
        return authorityName
    }

    fun setAuthorityName(authorityName: String?) {
        this.authorityName = sanitize(authorityName)
    }

    override fun getAuthorityOrganisation(): String? {
        return authorityOrganisation
    }

    fun setAuthorityOrganisation(authorityOrganisation: String?) {
        this.authorityOrganisation = sanitize(authorityOrganisation)
    }

    override fun getTimestamp(): LocalDateTime? {
        return timestamp
    }

    fun setTimestamp(dt: LocalDateTime?) {
        timestamp = dt
    }

    fun setTimestampMillis(millis: Long?) {
        if (millis==null) {
            timestamp = null
        } else {
            val instant = Instant.ofEpochMilli(millis)
            timestamp = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        }
    }

    override fun getSignatureType(): SignatureType? {
        return signatureType
    }

    fun setSignatureType(sigType: SignatureType?) {
        signatureType = sigType
    }

    override fun getSignature(): String? {
        return signature
    }

    fun setSignature(signature: String?) {
        this.signature = signature ?: ""
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("{")
        sb.append(jsonField("partyId", partyId)).append(", ")
        sb.append(jsonField("partyRole", partyRole)).append(", ")
        sb.append(jsonField("authorityName", authorityName)).append(", ")
        sb.append(jsonField("authorityOrganisation", authorityOrganisation)).append(", ")
        sb.append(jsonField("authorityCountry", if (authorityCountry!=null) authorityCountry!!.code else "")).append(", ")
        sb.append(jsonField("signatureType", if (signatureType!=null) signatureType!!.name else "")).append(", ")
        sb.append(jsonField("signature", signature)).append(", ")
        if (timestamp != null) {
            val ts = timestamp ?: LocalDateTime.now()
            sb.append(jsonField("timestamp", ts.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli().toString()))
        } else {
            sb.append(jsonField("timestamp", ""))
        }
        sb.append("}")
        return sb.toString()
    }

    private fun sanitize(value: String?): String {
        return if (value.isNullOrEmpty()) {
            ""
        } else {
            val noBreaks = removeChars(value, "\"\r" + FF + STX + ETX)
            val noBreaksNonNull = noBreaks ?: ""
            noBreaksNonNull.replace('\n', ' ')
        }
    }

    private fun jsonField(name: String, value: String?): String {
        return "\"" + name + "\":\"" + (value ?: "") + "\""
    }

}