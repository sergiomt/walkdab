package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.forms.Field
import com.clocial.walkdab.app.models.forms.FormsFactory
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

open class FormsFactoryPojo: FormsFactory {
    override fun createForm(): FormPojo {
        return FormPojo()
    }

    override fun createFormSet(): FormSetPojo {
        return FormSetPojo()
    }

    override fun createTab(): TabPojo {
        return TabPojo()
    }

    override fun createField(fieldName: String, fieldValue: Boolean?): FieldPojo {
        return createGenericField(fieldName, FieldValueBooleanPojo(fieldValue))
    }

    override fun createField(fieldName: String, fieldValue: String?): FieldPojo {
        return createGenericField(fieldName, FieldValueStringPojo(fieldValue))
    }

    override fun createField(fieldName: String,  fieldValue: LocalDate?): Field {
        return createGenericField(fieldName, FieldValueDatePojo(fieldValue))
    }

    override fun createField(fieldName: String,  fieldValue: ByteArray): Field {
        return createGenericField(fieldName, FieldValueBytesPojo(fieldValue))
    }

    override fun createField(fieldName: String, fieldValue: String, title: String): Field {
        return createGenericField(fieldName, FieldValueBookmarkPojo(title, fieldValue))
    }
    override fun createField(fieldName: String, fieldValue: String, expiration: LocalDateTime?): Field {
        return createGenericField(fieldName, FieldValuePasswordPojo(fieldValue, expiration))
    }
    private fun createGenericField(fieldName: String, fieldValue: Serializable?): FieldPojo {
        var newField = FieldPojo()
        newField.setName(fieldName)
        newField.setValue(fieldValue)
        return newField;
    }
}