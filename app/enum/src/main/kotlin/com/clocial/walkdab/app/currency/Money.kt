package com.clocial.walkdab.app.currency

import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import java.lang.NullPointerException
import java.text.DecimalFormat
import java.lang.NumberFormatException
import java.math.BigDecimal

import kotlin.text.StringBuilder
import kotlin.Throws

/**
 *
 * Combination of BigDecimal with Currency Sign
 * This class handles money amounts that include a currency sign
 * @author Sergio Montoro Ten
 * @version 1.0
 */
class Money : BigDecimal {
    private var oCurrCode: CurrencyCode? = null
    // ---------------------------------------------------------------------------
    /**
     * @param sVal String
     * @throws UnsupportedOperationException
     */
    private constructor(sVal: String) : super(sVal) {
        throw UnsupportedOperationException("Money(String) is not an allowed constructor")
    }
    // ---------------------------------------------------------------------------
    /**
     * Constructor that makes a copy from another Money value
     * @param oVal Money
     */
    constructor(oVal: Money) : super((oVal as BigDecimal).toString()) {
        oCurrCode = oVal.oCurrCode
    }
    // ---------------------------------------------------------------------------
    /**
     * Constructor
     * @param sVal String Numeric value in US decimal format (using dot as decimal delimiter)
     * @param oCur CurrencyCode
     * @throws NumberFormatException
     */
    constructor(sVal: String, oCur: CurrencyCode?) : super(sVal) {
        oCurrCode = oCur
    }
    // ---------------------------------------------------------------------------
    /**
     * Constructor
     * @param sVal String Numeric value in US decimal format (using dot as decimal delimiter)
     * @param sCur String Currency alphanumeric code {"USD", "EUR", etc.}
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     */
    constructor(sVal: String, sCur: String) : super(sVal) {
        oCurrCode = CurrencyCode.currencyCodeFor(sCur)
        requireNotNull(oCurrCode) { "Money() $sCur is not a legal currency alphanumeric code" }
    }
    // ---------------------------------------------------------------------------
    /**
     * Constructor
     * @param dVal double
     * @param oCur CurrencyCode
     */
    constructor(dVal: Double, oCur: CurrencyCode?) : super(dVal) {
        oCurrCode = oCur
    }
    // ---------------------------------------------------------------------------
    /**
     * Constructor
     * @param dVal double
     * @param sCur String Currency alphanumeric code {"USD", "EUR", etc.}
     * @throws IllegalArgumentException if currency code is not recognized
     */
    constructor(dVal: Double, sCur: String) : super(dVal) {
        oCurrCode = CurrencyCode.currencyCodeFor(sCur)
    }
    // ---------------------------------------------------------------------------
    /**
     * Constructor
     * @param oVal BigDecimal
     * @param sCur String Currency alphanumeric code {"USD", "EUR", etc.}
     * @throws IllegalArgumentException if currency code is not recognized
     */
    constructor(oVal: BigDecimal, sCur: String) : super(oVal.toString()) {
        oCurrCode = CurrencyCode.currencyCodeFor(sCur)
    }
    // ---------------------------------------------------------------------------
    /**
     * Constructor
     * @param oVal BigDecimal
     * @param oCur CurrencyCode
     */
    constructor(oVal: BigDecimal, oCur: CurrencyCode?) : super(oVal.toString()) {
        oCurrCode = oCur
    }

    // ---------------------------------------------------------------------------
    fun currencyCode(): CurrencyCode? {
        return oCurrCode
    }
    // ---------------------------------------------------------------------------
    /**
     * Compare to money amounts with the same currency
     * @param oMny Money
     * @return int Zero if this amount is equal to given amount.
     * Less than zero if if this amount is less than given amount.
     * More than zero if if this amount is greater than given amount.
     * @throws IllegalArgumentException If currency codes are not the same
     */
    @Throws(IllegalArgumentException::class)
    operator fun compareTo(oMny: Money): Int {
        return if (currencyCode()!!.equals(oMny.currencyCode()!!)) super.compareTo(oMny) else throw IllegalArgumentException(
            "Currency code " + currencyCode() + " does not match " + oMny.currencyCode()
        )
    } // compareTo
    // ---------------------------------------------------------------------------
    /**
     * Maximum of this Money instance and given Money instance
     * @param oMny Money
     * @return Money
     */
    fun max(oMny: Money): Money {
        return if (compareTo(oMny) < 0) oMny else this
    } // max
    // ---------------------------------------------------------------------------
    /**
     * Minimum of this Money instance and given Money instance
     * @param oMny Money
     * @return Money
     */
    fun min(oMny: Money): Money {
        return if (compareTo(oMny) < 0) this else oMny
    } // max
    // ---------------------------------------------------------------------------
    /**
     *
     * Add two money amounts
     * The return value is in the currency of this object.
     * If the added amount does not have the same currency
     * then it is converted by calling a web service before performing addition.
     * The scale of the returned value is max(this.scale(), oMny.scale()).
     * @return this.value + (to this currency) oMny.value
     * @throws NullPointerException If oMny is null
     * @throws IllegalArgumentException If currency codes are not the same
     */
    @Throws(NullPointerException::class, IllegalArgumentException::class)
    fun add(oMny: Money): Money {
        return if (oMny.signum() == 0) Money(this) else if (currencyCode()!!.equals(oMny.currencyCode()!!)) Money(
            super.add(oMny),
            currencyCode()
        ) else throw IllegalArgumentException("Currency code " + currencyCode() + " does not match " + oMny.currencyCode())
    } // add
    // ---------------------------------------------------------------------------
    /**
     *
     * Subtract two money amounts
     * The return value is in the currency of this object.
     * If the added amount does not have the same currency
     * then it is converted by calling a web service before performing addition.
     * The scale of the returned value is max(this.scale(), oMny.scale()).
     * @return this.value - (to this currency) oMny.value
     * @throws NullPointerException If oMny is null
     * @throws IllegalArgumentException If currency codes are not the same
     */
    @Throws(NullPointerException::class)
    fun subtract(oMny: Money): Money {
        return if (oMny.signum() == 0) Money(this) else if (currencyCode()!!.equals(oMny.currencyCode()!!)) Money(
            super.subtract(
                oMny
            ), currencyCode()
        ) else throw IllegalArgumentException("Currency code " + currencyCode() + " does not match " + oMny.currencyCode())
    } // subtract
    // ---------------------------------------------------------------------------
    /**
     * Rounds a BigDecimal value to two decimals
     * @return BigDecimal
     */
    fun round2(): Money {
        FMT2.maximumFractionDigits = 2
        return Money(FMT2.format(toDouble()).replace(',', '.'), oCurrCode)
    }
    // ---------------------------------------------------------------------------
    /**
     *
     * Convert **this** money to another currency
     * @param oTarget Target CurrencyCode
     * @param oRatio BigDecimal Conversion ratio
     * @return Money if **this** CurrencyCode is the same as oTarget
     * then **this** is returned without any modification,
     * if if **this** CurrencyCode is different from oTarget
     * then the returned value is **this** multiplied by oRatio.
     * @throws NullPointerException if oTarget is **null**
     */
    @Throws(NullPointerException::class)
    fun convertTo(oTarget: CurrencyCode?, oRatio: BigDecimal?): Money {
        val oNewVal: Money
        if (oTarget == null) throw NullPointerException("Money.convertTo() target currency cannot be null")
        if (oRatio == null) throw NullPointerException("Money.convertTo() conversion ratio cannot be null")
        oNewVal = if (oCurrCode != null) {
            if (oCurrCode!!.equals(oTarget)) this else Money(multiply(oRatio), oTarget)
        } else {
            Money(multiply(oRatio), oTarget)
        }
        return oNewVal
    } // convertTo
    // ---------------------------------------------------------------------------
    /**
     *
     * Convert **this** money to another currency
     * @param sTarget Target CurrencyCode
     * @param oRatio BigDecimal Conversion ratio
     * @return Money if **this** CurrencyCode is the same as oTarget
     * then **this** is returned without any modification,
     * if if **this** CurrencyCode is different from oTarget
     * then the returned value is **this** multiplied by oRatio.
     * @throws NullPointerException if oTarget is **null**
     */
    @Throws(NullPointerException::class, IllegalArgumentException::class)
    fun convertTo(sTarget: String?, oRatio: BigDecimal?): Money {
        if (sTarget == null) throw NullPointerException("Money.convertTo() target currency cannot be null")
        oCurrCode = CurrencyCode.currencyCodeFor(sTarget)
        requireNotNull(oCurrCode) { "Money.convertTo() $sTarget is not a legal currency alphanumeric code" }
        return convertTo(oCurrCode, oRatio)
    } // convertTo

    // ---------------------------------------------------------------------------
    /**
     * @return String BigDecimal.toString() followed by a single space and currencyCode().alphacode()
     */
    fun toLocaleString(): String {
        return if (oCurrCode == null) super.toString() else super.toString() + " " + oCurrCode!!.alphaCode()
    }

    override fun toByte(): Byte {
        TODO("Not yet implemented")
    }

    override fun toChar(): Char {
        TODO("Not yet implemented")
    }

    override fun toShort(): Short {
        TODO("Not yet implemented")
    }

    // ---------------------------------------------------------------------------
    override fun equals(other: Any?): Boolean {
        return other is Money && toLocaleString() == other.toLocaleString()
    }

    companion object {
        private const val serialVersionUID = 1L
        private val FMT2 = DecimalFormat()
        private val CurrencyStr = "(\\+|-)?([0-9]+)|([0-9]+.[0-9]+)".toRegex()

        // ---------------------------------------------------------------------------
        /**
         *
         * Checks whether the given string can be parsed as a valid Money value
         * Both comma and dot are allowed as either thousands or decimal delimiters.
         * If there is only a comma or a dot then it is assumed to be de decimal delimiter.
         * If both comma and dot are present, then the leftmost of them is assumed to
         * be the thousands delimiter and the rightmost is the decimal delimiter.
         * Any letters and currency symbols {€$£¤¢¥#ƒ&amp;} are ignored
         * @param sVal String
         * @return boolean
         */
        fun isMoney(sVal: String?): Boolean {
            if (sVal == null) return false
            if (sVal.isEmpty()) return false
            var sAmount = sVal.uppercase()
            val iDot = sAmount.indexOf('.')
            val iCom = sAmount.indexOf(',')
            if (iDot != 0 && iCom != 0) {
                if (iDot > iCom) {
                    sAmount.replace(",", "")
                } else {
                    sAmount.replace(".", "")
                }
            } // fi
            sAmount = sAmount.replace(',', '.')
            sAmount = removeChars(sAmount, "€$£¤¢¥#ƒ& ABCDEFGHIJKLMNOPQRSZUVWXYZ")!!
            return CurrencyStr.matches(sAmount)
        } // isMoney

        // ---------------------------------------------------------------------------
        /**
         *
         * Create a Money instance from a figure and currency
         * @param sVal String Must be of the form Currency Code + Figure or Figure + Currency Code. For example "23,5 EUR" or "GBP 12.8"
         * The figure may contain dot or comma as a decimal delimiter. € $ £ and ¥ are also recognized as currency codes
         * @return Money
         * @throws NullPointerException If String parameter is **null**
         * @throws IllegalArgumentException If String parameter is an empty String
         * @throws NumberFormatException
         */
        @Throws(NullPointerException::class, IllegalArgumentException::class, NumberFormatException::class)
        fun parse(sVal: String?): Money {
            val iDot: Int
            val iCom: Int
            var oCur: CurrencyCode? = null
            var sAmount: String
            if (null == sVal) throw NullPointerException("Money.parse() argument cannot be null")
            require(sVal.isNotEmpty()) { "Money.parse() argument cannot be an empty string" }
            sAmount = sVal.uppercase()
            if (sAmount.indexOf("EUR") >= 0 || sAmount.indexOf("€") >= 0 || sAmount.indexOf("&euro;") >= 0) oCur =
                CurrencyCode.EUR else if (sAmount.indexOf("USD") >= 0 || sAmount.indexOf("$") >= 0) oCur =
                CurrencyCode.USD else if (sAmount.indexOf("GBP") >= 0 || sAmount.indexOf("£") >= 0) oCur =
                CurrencyCode.GBP else if (sAmount.indexOf("JPY") >= 0 || sAmount.indexOf("YEN") >= 0 || sAmount.indexOf(
                    "¥"
                ) >= 0
            ) oCur = CurrencyCode.JPY else if (sAmount.indexOf("CNY") >= 0 || sAmount.indexOf("YUAN") >= 0) oCur =
                CurrencyCode.CNY
            iDot = sAmount.indexOf('.')
            iCom = sAmount.indexOf(',')
            if (iDot != 0 && iCom != 0) {
                if (iDot > iCom) {
                    sAmount.replace(",", "")
                } else {
                    sAmount.replace(".", "")
                }
            } // fi
            sAmount = sAmount.replace(',', '.')
            sAmount = removeChars(sAmount, "€$£¤¢¥#ƒ& ABCDEFGHIJKLMNOPQRSZUVWXYZ")!!
            return Money(sAmount, oCur)
        } // parse
        // ---------------------------------------------------------------------------
        /**
         * Remove a character set from a String
         * @param sInput Input String
         * @param sRemove A String containing all the characters to be removed from input String
         * @return The input String without all the characters of sRemove
         */
        private fun removeChars(sInput: String?, sRemove: String?): String? {
            if (null == sInput) return null
            if (null == sRemove) return sInput
            if (sInput.isEmpty()) return sInput
            if (sRemove.isEmpty()) return sInput
            val iLen = sInput.length
            val oOutput = StringBuilder(iLen)
            for (i in 0 until iLen) {
                val c = sInput[i]
                if (sRemove.indexOf(c) < 0) oOutput.append(c)
            } // next
            return oOutput.toString()
        } // removeChars
    }
}