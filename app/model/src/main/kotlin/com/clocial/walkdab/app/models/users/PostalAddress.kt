package com.clocial.walkdab.app.models.users

import com.clocial.walkdab.app.enums.locale.ISO3166CountryCode
import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.Deletable
import com.clocial.walkdab.app.models.snippets.GloballyUnique

interface PostalAddress : Auditable, Deletable, GloballyUnique {

    fun getCompanyName(companyName: String?)
    fun getContactPerson(): String?
    fun getStreetName(): String?
    fun getLine1(): String?
    fun getLine2(): String?
    fun getLine3(): String?
    fun getPostalCode(): String?
    fun getLocality(): String?
    fun getRegion(): String?
    fun getCountryName(countryName: String?)
    fun getCountryCode(): ISO3166CountryCode?
    fun getPhoneNumbers(): List<PhoneNumber?>?
    fun addPhoneNumber(phoneNumber: PhoneNumber?, isPreferred: Boolean)
    fun getPreferredPhoneNumber(): PhoneNumber?
    fun isActive(): Boolean
}