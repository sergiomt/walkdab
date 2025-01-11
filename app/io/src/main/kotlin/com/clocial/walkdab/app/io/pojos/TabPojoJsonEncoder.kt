package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.Tab
import com.clocial.walkdab.app.pojos.TabPojo
import com.clocial.walkdab.app.util.json.CustomEncoder
import java.time.LocalDateTime

class TabPojoJsonEncoder: JsonEncoderPojo(), CustomEncoder.Encoder<TabPojo> {

    private val fieldEncoder: FieldPojoJsonEncoder = FieldPojoJsonEncoder()

    override fun decode(nameValueMap: Map<String, Any>): TabPojo {
        val tab = TabPojo()
        val tabFieldsList = nameValueMap["tabFields"] as List<Map<String, Any>>
        tabFieldsList.map { j -> fieldEncoder.decode(j) }.forEach {
            f -> tab.add(f)
        }
        nameValueMap.keys.forEach {
            val keyVal = nameValueMap[it]
            when (it) {
                "tabId" -> tab.setId(keyVal as String)
                "tabName" -> keyVal?.let { it1 -> tab.setName(it1 as String) }
                "formId" -> keyVal?.let { it1 -> tab.setFormId(it1 as String) }
                "createdOn" -> keyVal?.let { it1 -> tab.setCreatedOn(LocalDateTime.parse(it1 as String)) }
                "createdBy" -> keyVal?.let { it1 -> tab.setCreatedBy(it1 as String) }
                "updatedOn" -> keyVal?.let { it1 -> tab.setUpdatedOn(LocalDateTime.parse(it1 as String)) }
                "updatedBy" -> keyVal?.let { it1 -> tab.setUpdatedBy(it1 as String) }
            }
        }
        return tab
    }

    override fun encode(builder: StringBuilder, r: Any?) {
        val t = r as Tab
        encodeFirstNameValue(builder, "tabId", t.getId())
        encodeNextNameValue (builder, "tabName", t.getName())
        encodeNextNameValue (builder, "formId", t.getFormId())
        encodeNextNameValue (builder, "createdOn", t.getCreatedOn())
        encodeNextNameValue (builder, "createdBy", t.getCreatedBy())
        encodeNextNameValue (builder, "updatedOn", t.getUpdatedOn())
        encodeNextNameValue (builder, "updatedBy", t.getUpdatedBy())
        encodeLastNameValue (builder, "tabFields", t.getFields())
    }
}