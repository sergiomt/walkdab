package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.enums.locale.ISO3166CountryCode
import com.clocial.walkdab.app.enums.security.SignatureType
import com.clocial.walkdab.app.pojos.SignaturePojo
import com.clocial.walkdab.app.util.json.CustomEncoder

import java.time.LocalDateTime

class SignaturePojoJsonEncoder: JsonEncoderPojo(), CustomEncoder.Encoder<SignaturePojo> {
    override fun decode(nameValueMap: Map<String, Any>): SignaturePojo {
        val sig = SignaturePojo();
        nameValueMap.keys.forEach {
            val keyVal = nil(nameValueMap[it])
            when (it) {
                "partyId" -> keyVal?.let { it1 -> sig.setPartyId(it1 as String) }
                "partyRole" -> keyVal?.let { it1 -> sig.setPartyRole(it1 as String) }
                "authorityName" -> keyVal?.let { it1 -> sig.setAuthorityName(it1 as String) }
                "authorityOrganisation" -> keyVal?.let { it1 -> sig.setAuthorityOrganisation(it1 as String) }
                "authorityCountry" -> keyVal?.let { it1 -> sig.setAuthorityCountry(ISO3166CountryCode.fromCode(it1 as String)) }
                "signatureType" -> keyVal?.let { it1 -> sig.setSignatureType(SignatureType.valueOf(it1 as String)) }
                "timestamp" -> keyVal?.let { it1 -> sig.setTimestamp(LocalDateTime.parse(it1 as String)) }
            }
        }
        return sig
    }

    override fun encode(builder: StringBuilder, r: Any?) {
        val s = r as SignaturePojo
        builder.append(s.toString())
    }
}