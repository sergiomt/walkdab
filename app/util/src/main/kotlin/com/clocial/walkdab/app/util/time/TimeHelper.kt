package com.clocial.walkdab.app.util.time

import com.clocial.walkdab.app.util.str.Str
import java.time.LocalDate
import java.time.LocalDateTime

object TimeHelper {

    fun formatCompactDate(dt: LocalDate): String {
        return dt.year.toString() + Str.leftPad(dt.monthValue.toString(),'0', 2) + Str.leftPad(dt.dayOfMonth.toString(),'0', 2)
    }

    fun parseCompactDate(representation: String): LocalDate {
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
    fun formatCompactTimestamp(dt: LocalDateTime?): String {
        val ts = dt ?: LocalDateTime.now()
        val sb = StringBuilder(14)
        sb.append(ts.year.toString())
        sb.append(ts.monthValue.toString())
        sb.append(ts.dayOfMonth.toString())
        sb.append(ts.hour.toString())
        sb.append(ts.minute.toString())
        sb.append(ts.second.toString())
        return sb.toString()
    }

    fun parseCompactTimestamp(st: String): LocalDateTime {
        return LocalDateTime.of(st.substring(0,4).toInt(), st.substring(4,6).toInt(), st.substring(6,8).toInt(),
            st.substring(8,10).toInt(), st.substring(10,12).toInt(), st.substring(12,14).toInt())
    }
}