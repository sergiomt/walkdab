package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.pojos.FieldPojo
import com.clocial.walkdab.app.pojos.FormPojo
import com.clocial.walkdab.app.pojos.FormsFactoryPojo
import com.clocial.walkdab.app.pojos.TabPojo
import com.clocial.walkdab.app.util.json.CustomEncoder
import com.clocial.walkdab.app.util.json.JSON

import org.junit.jupiter.api.Test
import java.time.LocalDate

class JsonTest {

    @Test
    fun testToJson() {

        val testFactory = FormsFactoryPojo()
        val testForm1 = testFactory.createForm()
        testForm1.addGeneralTag("tag1")
        testForm1.addGeneralTag("tag2")
        val testTab1 = testForm1.getFirstTab()
        testTab1.setId("T1")
        testTab1.setName("Tab 1")
        testTab1.add(testFactory.createField("FirstFieldName", "First Field Value"))
        testTab1.add(testFactory.createField("SecondFieldName", LocalDate.of(2025,11,1)))
        testTab1.add(testFactory.createField("ThirdFieldName", true))
        val testTab2 = testFactory.createTab()
        testTab2.setId("T2")
        testTab2.setName("Tab 2")
        testTab2.add(testFactory.createField("FourthFieldName", "Fourth Field Value"))
        testForm1.addTab(testTab1)
        testForm1.addTab(testTab2)
        val enc = CustomEncoder()
        enc.addEncoder(FieldPojo::class.java, FieldPojoJsonEncoder())
        enc.addEncoder(TabPojo::class.java, TabPojoJsonEncoder())
        enc.addEncoder(FormPojo::class.java, FormPojoJsonEncoder())
        val jsonOut = JSON.jsonifyCustom(testForm1, enc)
        print(jsonOut.toCharArray())

    }
}