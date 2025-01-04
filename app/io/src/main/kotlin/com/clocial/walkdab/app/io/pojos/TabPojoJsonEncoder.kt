package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.Tab

import com.clocial.walkdab.app.pojos.TabPojo
import com.clocial.walkdab.app.util.json.CustomEncoder

class TabPojoJsonEncoder: JsonEncoderPojo(), CustomEncoder.Encoder<TabPojo> {

    private val fieldEncoder: FieldPojoJsonEncoder = FieldPojoJsonEncoder()

    override fun decode(nameValueMap: Map<String, Any>): Any {
        val tab = TabPojo()
        val tabFieldsList = nameValueMap.get("tabFields") as List<Map<String, Any>>
        tabFieldsList.map { j -> fieldEncoder.decode(j) }.forEach{
            f -> tab.add(f)
        }
        nameValueMap.keys.forEach {
            val keyVal = nameValueMap.get(it)
            when (it) {
                "tabId" -> tab.setId(keyVal as String)
                "tabName" -> keyVal?.let { it1 -> tab.setName(it1 as String) }
                "formId" -> keyVal?.let { it1 -> tab.setFormId(it1 as String) }
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