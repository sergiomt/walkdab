package com.clocial.walkdab.app.models.snippets

import java.time.LocalDateTime

import com.clocial.walkdab.app.enums.locale.ISO3166CountryCode
import com.clocial.walkdab.app.enums.security.SignatureType

interface Signature {

    fun getPartyId(): String?

    fun getPartyRole(): String?

    fun getAuthorityName(): String?

    fun getAuthorityOrganisation(): String?

    fun getAuthorityCountry(): ISO3166CountryCode?

    fun getTimestamp(): LocalDateTime?

    fun getSignature(): String?

    fun getSignatureType(): SignatureType?

}