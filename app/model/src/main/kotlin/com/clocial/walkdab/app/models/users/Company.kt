package com.clocial.walkdab.app.models.users

import com.clocial.walkdab.app.enums.locale.ISO3166CountryCode

interface Company : com.clocial.walkdab.app.models.snippets.Auditable,
    com.clocial.walkdab.app.models.snippets.Deletable, com.clocial.walkdab.app.models.snippets.GloballyUnique {
    var legalName: String?
    var shortName: String?
    var country: ISO3166CountryCode?
    val addresses: List<com.clocial.walkdab.app.models.users.PostalAddress?>?
    val mainAddress: com.clocial.walkdab.app.models.users.PostalAddress?
}