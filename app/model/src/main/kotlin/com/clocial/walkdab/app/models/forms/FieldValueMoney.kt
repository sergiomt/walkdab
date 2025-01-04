package com.clocial.walkdab.app.models.forms

import com.clocial.walkdab.app.currency.Money

interface FieldValueMoney : FieldValue {

    override fun getValue(): Money?

}