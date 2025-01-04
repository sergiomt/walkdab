package com.clocial.walkdab.app.models.users

import com.clocial.walkdab.app.enums.dial.ITUCallingCode
import com.clocial.walkdab.app.enums.dial.PhoneNumberLocation

interface PhoneNumber {
    val callingCode: ITUCallingCode?
    var dialNumber: Long?
    var displayNumber: String?
    var location: PhoneNumberLocation?
}