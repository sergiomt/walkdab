package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.pojos.FieldPojo
import com.clocial.walkdab.app.pojos.FormsFactoryPojo
import com.clocial.walkdab.app.pojos.TabPojo
import com.clocial.walkdab.app.util.json.CustomEncoder
import com.clocial.walkdab.app.util.json.JSON

import org.junit.jupiter.api.Test

class JsonTest {

    @Test
    fun testToJson() {

        val testFactory = FormsFactoryPojo()
        val testForm = testFactory.createForm()
        val testTab = testFactory.createTab()
        testTab.add(testFactory.createField("Hello", "Hello World!"))
        testForm.addTab(testTab)
        val enc = CustomEncoder()
        enc.addEncoder(FieldPojo::class.java, FieldPojoJsonEncoder())
        enc.addEncoder(TabPojo::class.java, TabPojoJsonEncoder())
        val jsonOut = JSON.jsonifyCustom(testTab, enc)
        print(jsonOut.toCharArray())

    }
}