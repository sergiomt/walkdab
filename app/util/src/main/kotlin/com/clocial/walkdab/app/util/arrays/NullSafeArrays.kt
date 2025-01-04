package com.clocial.walkdab.app.util.arrays

object NullSafeArrays {

    fun arraysEquals(a: ByteArray?, b: ByteArray?): Boolean {
        if (a==null || b==null) return false
        if (a.size != b.size) return false
        if (a.isEmpty()) return true
        val l = a.size
        var i = 0
        do {
            if (a[i] != b[i]) return false
            i++
        } while (i<l)
        return true
    }

}