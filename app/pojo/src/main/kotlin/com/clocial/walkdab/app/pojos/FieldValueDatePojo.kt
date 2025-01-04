package com.clocial.walkdab.app.pojos

import java.io.Serializable
import java.time.LocalDate

import com.clocial.walkdab.app.models.forms.FieldValueDate

class FieldValueDatePojo : FieldValueDate, Serializable {

    private var theDate: LocalDate?

    constructor() {
        theDate = null
    }

    constructor(theDate: LocalDate?) {
        this.theDate = theDate
    }

    override fun toString(): String {
        val nonNullDate = theDate ?: LocalDate.now()
        return if (null == theDate) "" else nonNullDate.year.toString() + nonNullDate.monthValue.toString() + nonNullDate.dayOfMonth.toString()
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
        theDate = if (strRepresentation.isNullOrEmpty()) null else parse(strRepresentation)
    }

    override fun isSearchable(): Boolean {
        return true
    }

    override fun getValue(): LocalDate? {
        return theDate
    }

    private fun parse(representation: String): LocalDate {
        if (representation.length == 8) {
            return LocalDate.of(representation.substring(0,4).toInt(), representation.substring(4,6).toInt(),representation.substring(6).toInt())
        } else if (representation.length == 10) {
            val dateParts = representation.split("-")
            if (dateParts.get(0).length == 4) {
                return LocalDate.of(dateParts.get(0).toInt(), dateParts.get(1).toInt(), dateParts.get(2).toInt())
            } else {
                return LocalDate.of(dateParts.get(2).toInt(), dateParts.get(1).toInt(), dateParts.get(0).toInt())
            }
        }
        throw IllegalArgumentException("invalid date representation $representation")
    }

    private fun nullSafeEqDate(a: LocalDate?, b: LocalDate?): Boolean = if (a == null && b == null) true else if (a == null && b != null) false else if (a != null && b == null) false else a == b

    override fun equals(other: Any?): Boolean {
        return other is FieldValueDate && nullSafeEqDate(getValue(), other.getValue()) ||
               other is LocalDate && nullSafeEqDate(getValue(), other)
    }

}