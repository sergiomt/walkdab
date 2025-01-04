package com.clocial.walkdab.app.enums.locale

import kotlin.Throws
import java.lang.IllegalArgumentException

enum class ISO639_1LanguageCode(val code: String) {
    NONE(""), AFRIKAANS("af"), ARABIC("ar"), BELARUSIAN("be"), BULGARIAN("bg"), CATALAN("ca"), CZECH("cs"), DANISH("da"), GERMAN(
        "de"
    ),
    GREEK("el"), ENGLISH("en"), SPANISH("es"), ESTONIAN("et"), BASQUE("eu"), FARSI("fa"), FINNISH("fi"), FAEROESE("fo"), FRENCH(
        "fr"
    ),
    GAELIC("gd"), GALLEGAN("gl"), HEBREW("he"), HINDI("hi"), CROATIAN("hr"), HUNGARIAN("hu"), INDONESIAN("in"), ICELANDIC(
        "is"
    ),
    ITALIAN("it"), YIDDISH("ji"), JAPANESSE("ja"), KOREAN("ko"), LITHUANIAN("lt"), LATVIAN("lv"), MACEDONIAN("mk"), MALTESE(
        "mt"
    ),
    DUTCH("nl"), NORWEGIAN("no"), POLISH("pl"), PORTUGUESE("pt"), RHAETO_ROMAN("rm"), ROMANIAN("ro"), RUSSIAN("ru"), SLOVAK(
        "sk"
    ),
    SLOVENIAN("sl"), ALBANIAN("sq"), SERBIAN("sr"), SWEDISH("sv"), SUTU("sx"), SAMI("sz"), THAI("th"), TSWANA("tn"), TURKISH(
        "tr"
    ),
    TSONGA("ts"), UKRAINIAN("uk"), URDU("ur"), VENDA("ve"), VIETNAMESE("vi"), XHOSA("xh"), NEUTRAL("xx"), CHINESE("zh"), CHINESE_SIMPLIFIED(
        "tw"
    ),
    CHINESE_TRADITIONAL("cn");

    companion object {
        @JvmStatic
		@Throws(IllegalArgumentException::class)
        fun fromCode(code: String?): ISO639_1LanguageCode? {
            if (null == code) return null
            for (i in values()) if (i.code.equals(code, ignoreCase = true)) return i
            throw IllegalArgumentException("Invalid value for ISO 639-1 language code $code")
        }
    }
}