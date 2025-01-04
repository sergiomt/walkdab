package com.clocial.walkdab.app.pojos

import kotlin.collections.List

import com.clocial.walkdab.app.encoding.BaseA64
import com.clocial.walkdab.app.encoding.BaseZ85
import com.clocial.walkdab.app.models.forms.FieldValueList

class FieldValueListPojo : FieldValueList {

    val BASE_ENCODING = 85

    private var listValues: MutableList<String>

    constructor() {
        listValues = mutableListOf()
    }

    constructor(vararg values: String) {
        listValues = MutableList(values.size) { i: Int -> values[i] }
    }

    constructor(values: List<String>) {
        this.listValues = MutableList(values.size) { i: Int -> values[i] }
    }

    override fun setValue(values: Any?) {
        if (null == values) {
            this.listValues = mutableListOf()
        } else if (values is List<*>) {
            this.listValues = MutableList(values.size) { i: Int -> if (values[i]==null) "" else values[i].toString() }
        } else if (values is String) {
            setFromString(values as String?)
        } else {
            throw ClassCastException("Cannot cast " + values.javaClass.name + " to java.util.List<String>")
        }
    }

    @Throws(IllegalArgumentException::class)
    override fun setFromString(strRepresentation: String?) {
        if (strRepresentation==null || strRepresentation.isEmpty()) {
            listValues = mutableListOf()
        } else {
            val firstSemicolon = strRepresentation.indexOf(';')
            val secondSemicolon = strRepresentation.indexOf(';', firstSemicolon + 1)
            val itemCount = strRepresentation.substring(0, firstSemicolon).toInt()
            val baseEncoding = strRepresentation.substring(firstSemicolon + 1, secondSemicolon).toInt()
            assert(baseEncoding==85 || baseEncoding==64)
            if (itemCount > 0) {
                val values = strRepresentation.substring(secondSemicolon + 1).split(";")
                listValues = MutableList(values.size) { i: Int ->
                    if (values[i].isEmpty())
                        ""
                    else
                        String(if (baseEncoding==85) BaseZ85.decode(values[i]) else BaseA64.decode(values[i].toCharArray()), Charsets.UTF_8) }
            }
        }
    }

    override fun toString(): String {
        assert(BASE_ENCODING==85 || BASE_ENCODING==64)
        val buffer = StringBuilder()
        val listSize = listValues.size
        buffer.append(listSize.toString())
        buffer.append(";")
        buffer.append(BASE_ENCODING.toString())
        buffer.append(";")
        var v = 0
        while (v < listSize) {
            val clearTextValue = listValues.get(v++)
            if (clearTextValue.isNotEmpty()) {
                val textValueBytes = clearTextValue.toByteArray(Charsets.UTF_8)
                if (BASE_ENCODING==85) {
                    buffer.append(BaseZ85.encode(textValueBytes))
                } else {
                    buffer.append(BaseA64.encode(textValueBytes))
                }
            }
            if (v < listSize - 1) buffer.append(";")
        }
        return buffer.toString()
    }

    fun setValue(values: MutableList<String>) {
        this.listValues = MutableList(values.size) { i: Int -> values.get(i) }
    }

    override fun isSearchable(): Boolean {
        return false
    }

    override fun getValue(): List<String> {
        return listValues
    }

    override fun add(value: String) {
        if (null == listValues) {
            listValues = ArrayList()
        }
        listValues!!.add(value)
    }

    override fun remove(value: String) {
        if (null != listValues) {
            listValues!!.remove(value)
        }
    }

    override fun remove(index: Int) {
        listValues.removeAt(index)
    }

    override fun clear() {
        listValues.clear()
    }

    override fun size(): Int {
        return listValues.size
    }

    override fun equals(o: Any?): Boolean {
        var e = 0
        if (o is FieldValueList) {
            return equals(o.getValue())
        } else if (o is List<*>) {
            if (size() != o.size) {
                return false
            } else if (size() == 0) {
                return true
            } else {
                while (e < size()) {
                    if (!listValues.get(e).equals(o.get(e))) {
                        return false
                    }
                    e++
                }
                return true
            }
        } else {
            return false
        }
    }
}