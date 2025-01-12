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
            if (dateParts[0].length == 4) {
                return LocalDate.of(dateParts[0].toInt(), dateParts[1].toInt(), dateParts[2].toInt())
            } else {
                return LocalDate.of(dateParts[2].toInt(), dateParts[1].toInt(), dateParts[0].toInt())
            }
        }
        throw IllegalArgumentException("invalid date representation $representation")
    }
    fun formatCompactTimestamp(dt: LocalDateTime?): String {
        val ts = dt ?: LocalDateTime.now()
        val sb = StringBuilder(23)
        if (ts.year > 9999)
            sb.append("9999")
        else
            sb.append(ts.year.toString())
        sb.append(Str.leftPad(ts.monthValue.toString(),'0',2))
        sb.append(Str.leftPad(ts.dayOfMonth.toString(),'0',2))
        sb.append(Str.leftPad(ts.hour.toString(),'0',2))
        sb.append(Str.leftPad(ts.minute.toString(),'0',2))
        sb.append(Str.leftPad(ts.second.toString(),'0',2))
        sb.append(Str.rightPad(ts.nano.toString(),'0',9))
        return sb.toString()
    }

    fun parseCompactTimestamp(st: String): LocalDateTime {
        return LocalDateTime.of(st.substring(0,4).toInt(), st.substring(4,6).toInt(), st.substring(6,8).toInt(),
            st.substring(8,10).toInt(), st.substring(10,12).toInt(), st.substring(12,14).toInt(),
            st.substring(14).toInt())
    }
}