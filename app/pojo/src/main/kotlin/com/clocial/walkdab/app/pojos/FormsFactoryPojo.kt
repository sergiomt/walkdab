package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.forms.Field
import com.clocial.walkdab.app.models.forms.FormsFactory
import java.io.Serializable
import java.time.LocalDate

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

    private fun createGenericField(fieldName: String, fieldValue: Serializable?): FieldPojo {
        val newField = FieldPojo()
        newField.setName(fieldName)
        newField.setValue(fieldValue)
        return newField;
    }
}