package com.clocial.walkdab.app.pojos

import java.io.Serializable
import java.time.LocalDate

import com.clocial.walkdab.app.models.forms.FieldValueDate
import com.clocial.walkdab.app.util.time.TimeHelper.formatCompactDate
import com.clocial.walkdab.app.util.time.TimeHelper.parseCompactDate

class FieldValueDatePojo : FieldValueDate, Serializable {

    private var theDate: LocalDate?

    constructor() {
        this.theDate = null
    }

    constructor(theDate: LocalDate?) {
        this.theDate = theDate
    }

    constructor(yyyyMMdd: String) {
        this.theDate = null
        setFromString(yyyyMMdd)
    }

    override fun toString(): String {
        val nonNullDate = theDate ?: LocalDate.now()
        return if (null == theDate) "" else formatCompactDate(nonNullDate)

    }

    override fun setValue(value: Any?) {
        if (null == value) {
            theDate = null
        } else if (value is String) {
            setFromString(value as String?)
        } else if (value is LocalDate) {
            theDate = value
        } else {
            throw ClassCastException("Cannot cast from " + value.javaClass + " to LocalDate")
        }
    }

    override fun setFromString(strRepresentation: String?) {
        theDate = if (strRepresentation.isNullOrEmpty()) null else parseCompactDate(strRepresentation)
    }

    override fun isSearchable(): Boolean {
        return true
    }

    override fun getValue(): LocalDate? {
        return theDate
    }

    private fun nullSafeEqDate(a: LocalDate?, b: LocalDate?): Boolean = if (a == null && b == null) true else if (a == null && b != null) false else if (a != null && b == null) false else a == b

    override fun equals(other: Any?): Boolean {
        return other is FieldValueDate && nullSafeEqDate(getValue(), other.getValue()) ||
               other is LocalDate && nullSafeEqDate(getValue(), other)
    }

}