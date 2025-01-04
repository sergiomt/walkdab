package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.currency.CurrencyCode
import com.clocial.walkdab.app.currency.Money
import com.clocial.walkdab.app.models.forms.FieldValueMoney

class FieldValueMoneyPojo : FieldValueMoney {

    private var amount: Money?

    constructor() {
        this.amount = null
    }

    constructor(amount: Money?) {
        this.amount = amount
    }

    override fun setValue(value: Any?) {
        amount = if (null == value) {
            null
        } else if (value is Money) {
            Money((value as Money?)!!)
        } else if (value is String) {
            if (value.isEmpty()) {
                null
            } else {
                val space = value.indexOf(' ')
                Money(
                    value.substring(0, space),
                    CurrencyCode.currencyCodeFor(value.substring(space + 1))
                )
            }
        } else {
            throw ClassCastException("Cannot cast from " + value.javaClass.name + " to Money")
        }
    }

    override fun setFromString(strRepresentation: String?) {
        amount = if (strRepresentation.isNullOrEmpty()) {
            null
        } else {
            val space = strRepresentation.indexOf(' ')
            Money(
                strRepresentation.substring(0, space),
                CurrencyCode.currencyCodeFor(strRepresentation.substring(space + 1))
            )
        }
    }

    override fun toString(): String {
        return if (null == amount) "" else amount!!.toPlainString() + " " + amount!!.currencyCode()!!.alphaCode()
    }

    override fun isSearchable(): Boolean {
        return true
    }

    override fun getValue(): Money? {
        return amount
    }

    private fun nullSafeEqMoney(a: Money?, b: Money?): Boolean = if (a == null && b == null) true else if (a == null && b != null) false else if (a != null && b == null) false else a == b

    override fun equals(o: Any?): Boolean {
        return o is FieldValueMoney && nullSafeEqMoney(getValue(), o.getValue()) || o is Money && nullSafeEqMoney(getValue(), o as Money?)
    }
}