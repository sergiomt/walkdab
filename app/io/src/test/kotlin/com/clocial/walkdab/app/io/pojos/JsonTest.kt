package com.clocial.walkdab.app.io.pojos

import com.clocial.walkdab.app.models.forms.FieldValuePassword
import com.clocial.walkdab.app.pojos.*
import com.clocial.walkdab.app.util.json.CustomEncoder
import com.clocial.walkdab.app.util.json.JSON
import com.clocial.walkdab.app.util.time.TimeHelper.formatCompactTimestamp
import com.clocial.walkdab.app.util.time.TimeHelper.parseCompactTimestamp
import com.clocial.walkdab.app.util.time.TimeHelper.parseCompactDate

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertArrayEquals
import java.time.LocalDateTime

class JsonTest {

    @Test
    fun testToJson() {
        val testForm1 = getTestForm()
        val jsonOut = JSON.jsonifyCustom(testForm1, getCustomEncoder())
        print(jsonOut.toCharArray())

        val fromMap = JSON.parse(jsonOut) as Map<String, Any>
        val formEnc = FormPojoJsonEncoder()
        val formRead = formEnc.decode(fromMap)

        assertEquals("TestForm1Key", formRead.getKey())
        assertEquals("TestForm1TenantId", formRead.getTenantId())
        assertEquals("Tab 1", formRead.getFirstTab().getName())

        val firstField = formRead.getFirstTab().getFieldByName("FirstFieldName")
        val firstFieldValue = firstField?.getValue()
        assertEquals("First Field Value", firstFieldValue?.getValue())

        val passwordField = formRead.getFirstTab().getFieldByName("PsswrdFieldName")
        assertEquals("PsswrdFieldName", passwordField?.getName())
        val passwordFieldValue = passwordField?.getValue() as FieldValuePassword
        assertEquals("secret", passwordFieldValue.getValue())
        val date9999 = parseCompactTimestamp(formatCompactTimestamp(LocalDateTime.MAX))
        assertEquals(date9999, passwordFieldValue.getExpiresOn())

        val bookmarkField = formRead.getFirstTab().getFieldByName("BookmarkFieldName")
        val bookmarkFieldVal = bookmarkField?.getValue() as FieldValueBookmarkPojo
        assertEquals("BookmarkFieldName", bookmarkField?.getName())
        assertEquals("Wikipedia", bookmarkFieldVal.getValue().getTitle())
        assertEquals("http://wikipedia.org", bookmarkFieldVal.getValue().getUri())

        val fifthField = formRead.getTabs()[1].getFieldByName("FifthFieldName")
        val fifthFieldValue: ByteArray = EncodedData(fifthField?.getValue()?.getValue() as String).toBytes()
        assertArrayEquals(getTestByteArray(), fifthFieldValue)

    }
    private fun getTestForm(): FormPojo {
        val date9999 = parseCompactTimestamp(formatCompactTimestamp(LocalDateTime.MAX))
        val testFactory = FormsFactoryPojo()

        val testForm1 = testFactory.createForm()
        testForm1.setKey("TestForm1Key")
        testForm1.setTenantId("TestForm1TenantId")
        testForm1.addGeneralTag("TestTag1")
        testForm1.addGeneralTag("TestTag2")

        val testTab1 = testForm1.getFirstTab()
        testTab1.setId("T1")
        testTab1.setName("Tab 1")
        testTab1.add(testFactory.createField("FirstFieldName", "First Field Value"))
        testTab1.add(testFactory.createField("SecondFieldName", parseCompactDate("20251101")))
        testTab1.add(testFactory.createField("ThirdFieldName", true))
        testTab1.add(testFactory.createField("PsswrdFieldName", "secret", date9999))
        testTab1.add(testFactory.createField("BookmarkFieldName", "http://wikipedia.org", "Wikipedia"))

        val testTab2 = testFactory.createTab()
        testTab2.setId("T2")
        testTab2.setName("Tab 2")
        testTab2.add(testFactory.createField("FourthFieldName", "Fourth Field Value"))
        testTab2.add(testFactory.createField("FifthFieldName", getTestByteArray()))
        testForm1.addTab(testTab2)

        return testForm1
    }

    private fun getTestByteArray(): ByteArray {
        val bytes = ByteArray(8)
        for (i in 0..7) {
            bytes[i] = i.toByte()
        }
        return bytes;
    }
    private fun getCustomEncoder(): CustomEncoder {
        val enc = CustomEncoder()
        enc.addEncoder(FieldPojo::class.java, FieldPojoJsonEncoder())
        enc.addEncoder(TabPojo::class.java, TabPojoJsonEncoder())
        enc.addEncoder(FormPojo::class.java, FormPojoJsonEncoder())
        return enc;
    }
}