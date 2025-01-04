package com.clocial.walkdab.app.enums.locale

import kotlin.Throws
import java.lang.IllegalArgumentException

enum class ISO3166CountryCode(val code: String) {
    ANDORRA("ad"), UNITED_ARAB_EMIRATES("ae"), AFGHANISTAN("af"), ANTIGUA_AND_BARBUDA("ag"), ANGUILLA("ai"), ALBANIA("al"), ARMENIA(
        "am"
    ),
    NETHERLANDS_ANTILLES("an"), ANGOLA("ao"), ANTARCTICA("aq"), ARGENTINA("ar"), AMERICAN_SAMOA("as"), AUSTRIA("at"), AUSTRALIA(
        "au"
    ),
    ARUBA("aw"), AZERBAIDJAN("az"), BOSNIA_HERZEGOVINA("ba"), BARBADOS("bb"), BANGLADESH("bd"), BELGIUM("be"), BOTSWANA(
        "bw"
    ),
    BURKINA_FASO("bf"), BULGARIA("bg"), BAHRAIN("bh"), BURUNDI("bi"), BENIN("bj"), BERMUDA("bm"), BRUNEI("bn"), BOLIVIA(
        "bo"
    ),
    BRAZIL("br"), BAHAMAS("bs"), BHUTAN("bt"), BOUVET_ISLAND("bv"), BELARUS("by"), BELIZE("bz"), CANADA("ca"), COCOS_ISLANDS(
        "cc"
    ),
    CONGO("cd"), CENTRAL_AFRICAN_REPUBLIC("cf"), CONGO_DEMOCRATIC_REPUBLIC("cg"), SWITZERLAND("ch"), IVORY_COAST("ci"), COOK_ISLANDS(
        "ck"
    ),
    CHILE("cl"), CAMEROON("cm"), CHINA("cn"), COLOMBIA("co"), COSTA_RICA("cr"), SLOVAKIA("sk"), CUBA("cu"), CAPE_VERDE("cv"), CHRISTMAS_ISLAND(
        "cx"
    ),
    CYPRUS("cy"), CZECH_REPUBLIC("cz"), DJIBOUTI("dj"), DENMARK("dk"), DOMINICA("dm"), DOMINICAN_REPUBLIC("do"), ALGERIA(
        "dz"
    ),
    ECUADOR("ec"), ERITREA("er"), ESTONIA("ee"), EGYPT("eg"), WESTERN_SAHARA("eh"), SPAIN("es"), ETHIOPIA("et"), FINLAND(
        "fi"
    ),
    FIJI_ISLANDS("fj"), FALKLAND_ISLANDS("fk"), MICRONESIA("fm"), FAROE_ISLANDS("fo"), FRANCE("fr"), GABON("ga"), GREAT_BRITAIN(
        "gb"
    ),
    GRENADA("gd"), GEORGIA("ge"), GERMANY("de"), FRENCH_GUYANA("gf"), GHANA("gh"), GIBRALTAR("gi"), GREENLAND("gl"), GAMBIA(
        "gm"
    ),
    GUINEA("gn"), GUADELOUPE_FRENCH("gp"), EQUATORIAL_GUINEA("gq"), GREECE("gr"), S_GEORGIA_S_SANDWICH("gs"), GUATEMALA(
        "gt"
    ),
    GUAM("gu"), GUINEA_BISSAU("gw"), GUYANA("gy"), HONG_KONG("hk"), HEARD_AND_MCDONALD_ISLANDS("hm"), HONDURAS("hn"), CROATIA(
        "hr"
    ),
    HAITI("ht"), HUNGARY("hu"), INDONESIA("id"), IRELAND("ie"), ISRAEL("il"), INDIA("in"), INTERNATIONAL("int"), BRITISH_INDIAN_OCEAN_TERRITORY(
        "io"
    ),
    IRAQ("iq"), IRAN("ir"), ICELAND("is"), ITALY("it"), JAMAICA("jm"), JORDAN("jo"), JAPAN("jp"), KENYA("ke"), KYRGYZSTAN(
        "kg"
    ),
    CAMBODIA("kh"), KIRIBATI("ki"), COMOROS("km"), SAINT_KITTS_NEVIS_ANGUILLA("kn"), NORTH_KOREA("kp"), SOUTH_KOREA("kr"), KUWAIT(
        "kw"
    ),
    CAYMAN_ISLANDS("ky"), KAZAKHSTAN("kz"), LAOS("la"), LEBANON("lb"), SAINT_LUCIA("lc"), LIECHTENSTEIN("li"), SRI_LANKA(
        "lk"
    ),
    LIBERIA("lr"), LESOTHO("ls"), LITHUANIA("lt"), LUXEMBOURG("lu"), LATVIA("lv"), LIBYA("ly"), MOROCCO("ma"), MONACO("mc"), MOLDAVIA(
        "md"
    ),
    MADAGASCAR("mg"), MARSHALL_ISLANDS("mh"), MACEDONIA("mk"), MALI("ml"), MYANMAR("mm"), MONGOLIA("mn"), MACAU("mo"), NORTHERN_MARIANA_ISLANDS(
        "mp"
    ),
    MARTINIQUE_FRENCH("mq"), MAURITANIA("mr"), MONTSERRAT("ms"), MALTA("mt"), MAURITIUS("mu"), MALDIVES("mv"), MALAWI("mw"), MEXICO(
        "mx"
    ),
    MALAYSIA("my"), MOLDOVA("md"), MONTENEGRO("me"), MOZAMBIQUE("mz"), NAMIBIA("na"), NEW_CALEDONIA("nc"), NIGER("ne"), NETWORK(
        "net"
    ),
    NIGERIA("ng"), NICARAGUA("ni"), NETHERLANDS("nl"), NORWAY("no"), NEPAL("np"), NAURU("nr"), NEUTRAL_ZONE("nt"), NIUE(
        "nu"
    ),
    NEW_ZEALAND("nz"), OMAN("om"), PALESTINE("ps"), PANAMA("pa"), PERU("pe"), POLYNESIA_FRENCH("pf"), PAPUA_NEW_GUINEA("pg"), PHILIPPINES(
        "ph"
    ),
    PAKISTAN("pk"), POLAND("pl"), SAINT_PIERRE_AND_MIQUELON("pm"), PITCAIRN_ISLAND("pn"), PUERTO_RICO("pr"), PORTUGAL("pt"), PALAU(
        "pw"
    ),
    PARAGUAY("py"), QATAR("qa"), ROMANIA("ro"), SERBIA("rs"), RUSSIA("ru"), RWANDA("rw"), SAUDI_ARABIA("sa"), SOLOMON_ISLANDS(
        "sb"
    ),
    SEYCHELLES("sc"), SUDAN("sd"), SOUTH_SUDAN("ss"), SWEDEN("se"), SINGAPORE("sg"), SLOVENIA("si"), SVALBARD_AND_JAN_MAYEN_ISLAND(
        "sj"
    ),
    SLOVAK_REPUBLIC("sk"), SIERRA_LEONE("sl"), SAN_MARINO("sm"), SENEGAL("sn"), SOMALIA("so"), SURINAME("sr"), SAINT_TOME(
        "st"
    ),
    FORMER_USSR("su"), EL_SALVADOR("sv"), SYRIA("sy"), SWAZILAND("sz"), TURKS_AND_CAICOS_ISLANDS("tc"), CHAD("td"), FRENCH_SOUTHERN_TERRITORIES(
        "tf"
    ),
    TOGO("tg"), THAILAND("th"), TADJIKISTAN("tj"), TOKELAU("tk"), TURKMENISTAN("tm"), TUNISIA("tn"), TONGA("to"), EAST_TIMOR(
        "tp"
    ),
    TURKEY("tr"), TRINIDAD_AND_TOBAGO("tt"), TUVALU("tv"), TAIWAN("tw"), TANZANIA("tz"), UKRAINE("ua"), UGANDA("ug"), UNITED_KINGDOM(
        "uk"
    ),
    USA_MINOR_OUTLYING_ISLANDS("um"), UNITED_STATES("us"), URUGUAY("uy"), UZBEKISTAN("uz"), VATICAN("va"), SAINT_VINCENT_GRENADINES(
        "vc"
    ),
    VENEZUELA("ve"), VIRGIN_ISLANDS_BRITISH("vg"), VIRGIN_ISLANDS_USA("vi"), VIETNAM("vn"), VANUATU("vu"), WALLIS_AND_FUTUNA_ISLANDS(
        "wf"
    ),
    SAMOA("ws"), YEMEN("ye"), MAYOTTE("yt"), YUGOSLAVIA("yu"), SOUTH_AFRICA("za"), ZAMBIA("zm"), ZAIRE("zr"), ZIMBABWE("zw");

    companion object {
        @JvmStatic
        @Throws(IllegalArgumentException::class)
        fun fromCode(code: String?): ISO3166CountryCode? {
            if (null == code) return null
            for (i in values()) if (i.code.equals(code, ignoreCase = true)) return i
            throw IllegalArgumentException("Invalid value for 3166 country code $code")
        }
    }
}