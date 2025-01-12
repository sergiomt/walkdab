package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.Form
import com.clocial.walkdab.app.pojos.FormPojo
import com.clocial.walkdab.app.util.json.CustomEncoder
import com.clocial.walkdab.app.util.time.TimeHelper.parseCompactTimestamp
class FormPojoJsonEncoder : JsonEncoderPojo(), CustomEncoder.Encoder<FormPojo> {

    private val tabEncoder: TabPojoJsonEncoder = TabPojoJsonEncoder()
    override fun decode(nameValueMap: Map<String, Any>): FormPojo {
        val frm = FormPojo();
        frm.removeTab(0)

        val frmTabsList = nameValueMap["formTabs"] as List<Map<String, Any>>
        frmTabsList.map { j -> tabEncoder.decode(j) }.forEach { k -> frm.addTab(k) }

        nameValueMap.keys.forEach {
            val keyVal = nil(nameValueMap[it])
            when (it) {
                "formKey" -> keyVal?.let { it1 -> frm.setKey(it1 as String) }
                "formTenantId" -> keyVal?.let { it1 -> frm.setTenantId(it1 as String) }
                "formName" -> keyVal?.let { it1 -> frm.setName(it1 as String) }
                "formSignature" -> keyVal?.let { it1 -> frm.setSignature(SignaturePojoJsonEncoder().decode(it1 as Map<String,Any>)) }
                "createdOn" -> keyVal?.let { it1 -> frm.setCreatedOn(parseCompactTimestamp(it1 as String)) }
                "createdBy" -> keyVal?.let { it1 -> frm.setCreatedBy(it1 as String) }
                "updatedOn" -> keyVal?.let { it1 -> frm.setUpdatedOn(parseCompactTimestamp(it1 as String)) }
                "updatedBy" -> keyVal?.let { it1 -> frm.setUpdatedBy(it1 as String) }
            }
        }
        return frm
    }
    override fun encode(builder: StringBuilder, r: Any?) {
        val f = r as Form
        encodeFirstNameValue(builder, "formKey", f.getKey())
        encodeNextNameValue (builder, "formTenantId", f.getTenantId())
        encodeNextNameValue (builder, "formName", f.getName())
        encodeNextNameValue (builder, "formSignature", f.getSignature())
        encodeNextNameValue (builder, "createdOn", f.getCreatedOn())
        encodeNextNameValue (builder, "createdBy", f.getCreatedBy())
        encodeNextNameValue (builder, "updatedOn", f.getUpdatedOn())
        encodeNextNameValue (builder, "updatedBy", f.getUpdatedBy())
        encodeNextNameValue (builder, "deleted", f.isDeleted())
        encodeNextNameValue (builder, "formTabs", f.getTabs())
        encodeLastNameValue (builder, "formTags", f.getGeneralTags())
    }

}