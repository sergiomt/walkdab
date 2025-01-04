package com.clocial.walkdab.app.currency

/**
 *
 * Currency Code
 * @author Sergio Montoro Ten
 * @version 1.0
 */
class CurrencyCode (
    private val iNumericCode: Int,
    private val sAlphaCode: String,
    private val sSignCode: String?,
    private val sIdEntity: String,
    private val sNmEntity: String,
    private val sNmCurrencyEn: String
) {
    private class CurrencyCodeComparator : Comparator<CurrencyCode> {
        override fun compare(oCurr1: CurrencyCode, oCurr2: CurrencyCode): Int {
            return oCurr1.toString().compareTo(oCurr2.toString(), ignoreCase = true)
        }
    }

    // ---------------------------------------------------------------------------
    /**
     * @return String ISO-639 two letter country code in lower case
     */
    fun countryCode(): String {
        return sIdEntity.lowercase()
    }
    // ---------------------------------------------------------------------------
    /**
     * @return Three letter currency code in upper case
     */
    fun alphaCode(): String {
        return sAlphaCode.uppercase()
    }
    // ---------------------------------------------------------------------------
    /**
     * @return String A single sign like $ € £ ¥
     */
    fun singleCharSign(): String? {
        return sSignCode
    }
    // ---------------------------------------------------------------------------
    /**
     * Currency name in English language
     * @return String
     */
    fun currencyName(): String {
        return sNmCurrencyEn
    }
    // ---------------------------------------------------------------------------
    /**
     * @return int Currency numeric code
     */
    fun numericCode(): Int {
        return iNumericCode
    }
    // ---------------------------------------------------------------------------
    /**
     * Two currencies will be the same if the have the same alphaCode()
     * @param oCurCod CurrencyCode
     * @return boolean
     */
    fun equals(oCurCod: CurrencyCode): Boolean {
        return if (sAlphaCode == null || oCurCod.alphaCode() == null) false else sAlphaCode == oCurCod.alphaCode()
    }

    //---------------------------------------------------------------------------
    override fun hashCode(): Int {
        return sAlphaCode?.hashCode() ?: 0
    }
    // ---------------------------------------------------------------------------
    /**
     * @return String Currency three letter currency code in upper case.
     */
    override fun toString(): String {
        return sAlphaCode
    }

    // ---------------------------------------------------------------------------

    private fun currencyCodeComparator(): CurrencyCodeComparator {
        return oCurrCodeComp
    }


    // ---------------------------------------------------------------------------

    private val oCurrCodeComp: CurrencyCodeComparator = CurrencyCodeComparator()

    companion object {
        /**
         * Get CurrencyCode for a 3 letter currency identifier
         * @param sAlphaCode
         * @return CurrencyCode instance for given code or **null** if no currency was found for that code
         */
        fun currencyCodeFor(sAlphaCode: String): CurrencyCode? {
            var oCurrCode: CurrencyCode?
            var iTableIndex: Int
            if (sAlphaCode != null) {
                if (sAlphaCode.equals("EUR", ignoreCase = true)) oCurrCode = EUR else if (sAlphaCode.equals(
                        "USD",
                        ignoreCase = true
                    )
                ) oCurrCode = USD else if (sAlphaCode.equals("GBP", ignoreCase = true)) oCurrCode =
                    GBP else if (sAlphaCode.equals("JPY", ignoreCase = true)) oCurrCode =
                    JPY else if (sAlphaCode.equals("CNY", ignoreCase = true)) oCurrCode =
                    CNY else if (sAlphaCode.equals("RUB", ignoreCase = true)) oCurrCode = RUB else {
                    oCurrCode = CurrencyCode(0, sAlphaCode, "", "", "", "")
                    iTableIndex = -1
                    var i = 0
                    for (c in Table) {
                        if (c.currencyCodeComparator().equals(oCurrCode)) {
                            iTableIndex = i
                            break
                        }
                        i++
                    }
                    oCurrCode = if (iTableIndex >= 0) Table[iTableIndex] else null
                }
            } else {
                oCurrCode = null
            }
            return oCurrCode
        } // currencyCodeFor

        // ---------------------------------------------------------------------------
        fun currencyCodeFor(iNumCode: Int): CurrencyCode? {
            val iCount = Table.size
            var oCurrCode: CurrencyCode? = null
            if (iNumCode == 978) oCurrCode = EUR else if (iNumCode == 840) oCurrCode =
                USD else if (iNumCode == 826) oCurrCode = GBP else if (iNumCode == 392) oCurrCode =
                JPY else if (iNumCode == 156) oCurrCode = CNY else if (iNumCode == 643) oCurrCode = RUB else {
                var c = 0
                while (c < iCount && oCurrCode == null) {
                    if (Table[c].iNumericCode == iNumCode) oCurrCode = Table[c]
                    c++
                }
            }
            return oCurrCode
        } // currencyCodeFor

        // ---------------------------------------------------------------------------
        @JvmField
        val CNY = CurrencyCode(156, "CNY", "¤", "cn", "China", "Yuan Renminbi")
        @JvmField
        val EUR = CurrencyCode(978, "EUR", "€", "eec", "European Economic Comunity", "Euro")
        @JvmField
        val GBP = CurrencyCode(826, "GBP", "£", "uk", "United Kingdom", "Pound Sterling")
        @JvmField
        val JPY = CurrencyCode(392, "JPY", "¥", "jp", "Japan", "Yen")
        @JvmField
        val USD = CurrencyCode(840, "USD", "$", "us", "United States", "US Dollar")
        @JvmField
        val RUB = CurrencyCode(643, "RUB", "R", "ru", "Russia", "Russian Ruble")

        // ---------------------------------------------------------------------------
        private val Table = arrayOf(
            CurrencyCode(20, "ADP", "₧", "ad", "Andorra", "Andorran Peseta"),
            CurrencyCode(784, "AED", "¤", "ae", "United Arab Emirates", "UAE Dirham"),
            CurrencyCode(4, "AFA", "¤", "af", "Afghanistan", "Afghani"),
            CurrencyCode(8, "ALL", "¤", "al", "Albania", "Lek"),
            CurrencyCode(51, "AMD", "¤", "am", "Armenia", "Armenian Dram"),
            CurrencyCode(532, "ANG", "ƒ", "an", "Netherlands Antilles", "Antillian Guilder"),
            //new CurrencyCode(982,"AOR","¤","ao","Angola","Kwanza Reajustado"),
            CurrencyCode(32, "ARS", "¤", "ar", "Argentina", "Argentine Peso"),
            CurrencyCode(40, "ATS", "¤", "as", "Austria", "Schilling"),
            CurrencyCode(36, "AUD", "$", "au", "Australia", "Australian Dollar"),
            CurrencyCode(533, "AWG", "ƒ", "aw", "Aruba", "Aruban Guilder"),
            CurrencyCode(31, "AZM", "¤", "az", "Azerbaijan", "Azerbaijanian Manat"),
            CurrencyCode(977, "BAM", "¤", "ba", "Bosnia-Herzegovina", "Convertible Marks"),
            CurrencyCode(52, "BBD", "$", "bb", "Barbados", "Barbados Dollar"),
            CurrencyCode(50, "BDT", "¤", "bd", "Bangladesh", "Taka"),
            CurrencyCode(56, "BEF", "¤", "be", "Belgium", "Belgian Franc"),
            CurrencyCode(975, "BGN", "¤", "bg", "Bulgaria", "Bulgarian Lev"),
            CurrencyCode(48, "BHD", "¤", "bh", "Bahrain", "Bahraini Dinar"),
            CurrencyCode(108, "BIF", "¤", "bi", "Burundi", "Burundi Franc"),
            CurrencyCode(60, "BMD", "¤", "bm", "Bermuda", "Bermudian Dollar"),
            CurrencyCode(96, "BND", "¤", "bn", "Brunei Darussalam", "Brunei Dollar"),
            CurrencyCode(986, "BRL", "¤", "br", "Brazil", "Brazilian Real"),
            CurrencyCode(44, "BSD", "¤", "bs", "Bahamas", "Bahamian Dollar"),
            CurrencyCode(64, "BTN", "¤", "bt", "Bhutan", "Ngultrum"),
            CurrencyCode(72, "BWP", "¤", "", "Botswana", "Pula"),
            CurrencyCode(974, "BYR", "¤", "by", "Belarus", "Belarussian Ruble"),
            CurrencyCode(84, "BZD", "¤", "bz", "Belize", "Belize Dollar"),
            CurrencyCode(124, "CAD", "¤", "ca", "Canada", "Canadian Dollar"),
            CurrencyCode(976, "CDF", "¤", "cg", "Congo, The Democratic Republic Of", "Franc Congolais"),
            CurrencyCode(756, "CHF", "₣", "ch", "Switzerland", "Swiss Franc"),
            CurrencyCode(152, "CLP", "¤", "cl", "Chile", "Chilean Peso"),
            CNY,
            CurrencyCode(170, "COP", "₱", "co", "Colombia", "Colombian Peso"),
            CurrencyCode(188, "CRC", "¤", "cr", "Costa Rica", "Costa Rican Colon"),
            CurrencyCode(192, "CUP", "₱", "cu", "Cuba", "Cuban Peso"),
            CurrencyCode(132, "CVE", "¤", "cv", "Cape Verde", "Cape Verde Escudo"),
            CurrencyCode(196, "CYP", "¤", "cy", "Cyprus", "Cyprus Pound"),
            CurrencyCode(203, "CZK", "¤", "cz", "Czech Republic", "Czech Koruna"),
            CurrencyCode(280, "DEM", "¤", "nu", "Germany", "Deutsche Mark"),
            CurrencyCode(262, "DJF", "¤", "dj", "Djibouti", "Djibouti Franc"),
            CurrencyCode(208, "DKK", "¤", "dk", "Denmark", "Danish Krone"),
            CurrencyCode(214, "DOP", "₱", "do", "Dominican Republic", "Dominican Peso"),
            CurrencyCode(12, "DZD", "¤", "dz", "Algeria", "Algerian Dinar"),
            // CurrencyCode(218,"ECS","¤","ec","Ecuador","Sucre"),
            CurrencyCode(233, "EEK", "¤", "ee", "Estonia", "Kroon"),
            CurrencyCode(818, "EGP", "£", "eg", "Egypt", "Egyptian Pound"),
            CurrencyCode(232, "ERN", "¤", "nu", "Eritrea", "Nakfa"),
            CurrencyCode(724, "ESP", "₧", "es", "Spain", "Spanish Peseta"),
            CurrencyCode(230, "ETB", "¤", "et", "Ethiopia", "Ethiopian Birr"),
            EUR,
            CurrencyCode(246, "FIM", "¤", "fi", "Finland", "Markka"),
            CurrencyCode(242, "FJD", "¤", "fj", "Fiji", "Fiji Dollar"),
            CurrencyCode(238, "FKP", "¤", "fk", "Falkland Islands", "Pound"),
            CurrencyCode(250, "FRF", "F", "fr", "France", "French Franc"),
            GBP,
            CurrencyCode(981, "GEL", "¤", "ge", "Georgia", "Lari"),
            CurrencyCode(288, "GHC", "¤", "gh", "Ghana", "Cedi"),
            CurrencyCode(292, "GIP", "£", "gi", "Gibraltar", "Gibraltar Pound"),
            CurrencyCode(270, "GMD", "¤", "gm", "Gambia", "Dalasi"),
            CurrencyCode(324, "GNF", "F", "gn", "Guinea", "Guinea Franc"),
            CurrencyCode(300, "GRD", "₯", "gr", "Greece", "Drachma"),
            CurrencyCode(320, "GTQ", "¤", "gt", "Guatemala", "Quetzal"),
            CurrencyCode(624, "GWP", "₱", "gw", "Guinea-Bissau", "Guinea-Bissau Peso"),
            CurrencyCode(328, "GYD", "$", "gy", "Guyana", "Guyana Dollar"),
            CurrencyCode(344, "HKD", "$", "hk", "Hong Kong", "Hong Kong Dollar"),
            CurrencyCode(340, "HNL", "¤", "hn", "Honduras", "Lempira"),
            CurrencyCode(191, "HRK", "¤", "hr", "Croatia", "Kuna"),
            CurrencyCode(332, "HTG", "¤", "ht", "Haiti", "Gourde"),
            CurrencyCode(348, "HUF", "ƒ", "hu", "Hungary", "Forint"),
            CurrencyCode(360, "IDR", "₨", "id", "Indonesia", "Rupiah"),
            CurrencyCode(360, "IDR", "₨", "tp", "East Timor", "Rupiah"),
            CurrencyCode(372, "IEP", "£", "ie", "Ireland", "Irish Pound"),
            CurrencyCode(376, "ILS", "₪", "il", "Israel", "New Israeli Sheqel"),
            CurrencyCode(356, "INR", "₨", "in", "India", "Indian Rupee"),
            CurrencyCode(368, "IQD", "¤", "iq", "Iraq", "Iraqi Dinar"),
            CurrencyCode(364, "IRR", "¤", "ir", "Iran", "Iranian Rial"),
            CurrencyCode(352, "ISK", "¤", "is", "Iceland", "Iceland Krona"),
            CurrencyCode(380, "ITL", "£", "it", "Italy", "Italian Lira"),
            CurrencyCode(388, "JMD", "$", "jm", "Jamaica", "Jamaican Dollar"),
            CurrencyCode(400, "JOD", "¤", "jo", "Jordan", "Jordanian Dinar"),
            JPY,
            CurrencyCode(404, "KES", "¤", "ke", "Kenya", "Kenyan Shilling"),
            CurrencyCode(417, "KGS", "¤", "kg", "Kyrgyzstan", "Som"),
            CurrencyCode(116, "KHR", "¤", "kh", "Cambodia, Kingdom of", "Riel"),
            CurrencyCode(174, "KMF", "¤", "km", "Comoros", "Comoro Franc"),
            CurrencyCode(408, "KPW", "₩", "kp", "Korea, Democratic People's Republic Of", "North Korean Won"),
            CurrencyCode(410, "KRW", "₩", "kr", "Korea, Republic Of", "Won"),
            CurrencyCode(414, "KWD", "¤", "kw", "Kuwait", "Kuwaiti Dinar"),
            CurrencyCode(136, "KYD", "¤", "ky", "Cayman Islands", "Cayman Islands Dollar"),
            CurrencyCode(398, "KZT", "¤", "kz", "Kazakhstan", "Tenge"),
            CurrencyCode(418, "LAK", "₭", "la", "Lao People's Democratic Republic", "Kip"),
            CurrencyCode(422, "LBP", "¤", "lb", "Lebanon", "Lebanese Pound"),
            CurrencyCode(144, "LKR", "₨", "lk", "Sri Lanka", "Sri Lanka Rupee"),
            CurrencyCode(430, "LRD", "¤", "lr", "Liberia", "Liberian Dollar"),
            CurrencyCode(426, "LSL", "¤", "ls", "Lesotho", "Loti"),
            CurrencyCode(440, "LTL", "¤", "lt", "Lithuania", "Lithuanian Litas"),
            CurrencyCode(442, "LUF", "¤", "lu", "Luxembourg", "Luxembourg Franc"),
            CurrencyCode(428, "LVL", "¤", "lv", "Latvia", "Latvian Lats"),
            CurrencyCode(434, "LYD", "¤", "ly", "Libyan Arab Jamahiriya", "Libyan Dinar"),
            CurrencyCode(504, "MAD", "¤", "ma", "Morocco", "Moroccan Dirham"),
            CurrencyCode(498, "MDL", "¤", "md", "Republic Of Moldova", "Moldovan Leu"),
            CurrencyCode(450, "MGF", "¤", "mg", "Madagascar", "Malagasy Franc"),
            CurrencyCode(807, "MKD", "¤", "mk", "Macedonia, The Former Yugoslav Republic Of", "Denar"),
            CurrencyCode(104, "MMK", "¤", "mm", "Myanmar", "Kyat"),
            CurrencyCode(496, "MNT", "₮", "mn", "Mongolia", "Tugrik"),
            CurrencyCode(446, "MOP", "¤", "mo", "Macau", "Pataca"),
            CurrencyCode(478, "MRO", "¤", "mr", "Mauritania", "Ouguiya"),
            CurrencyCode(470, "MTL", "¤", "mt", "Malta", "Maltese Lira"),
            CurrencyCode(480, "MUR", "¤", "mu", "Mauritius", "Mauritius Rupee"),
            CurrencyCode(462, "MVR", "¤", "mv", "Maldives", "Rufiyaa"),
            CurrencyCode(454, "MWK", "¤", "mw", "Malawi", "Kwacha"),
            CurrencyCode(484, "MXN", "₱", "mx", "Mexico", "Mexican Peso"),
            CurrencyCode(979, "MXV", "¤", "mx", "Mexico", "Mexican Unidad de Inversion (UDI)"),
            CurrencyCode(458, "MYR", "¤", "my", "Malaysia", "Malaysian Ringgit"),
            CurrencyCode(508, "MZM", "¤", "mz", "Mozambique", "Metical"),
            CurrencyCode(516, "NAD", "¤", "na", "Namibia", "Namibia Dollar"),
            CurrencyCode(566, "NGN", "¤", "ng", "Nigeria", "Naira"),
            CurrencyCode(558, "NIO", "¤", "ni", "Nicaragua", "Cordoba Oro"),
            CurrencyCode(528, "NLG", "¤", "nl", "Netherlands", "Netherlands Guilder"),
            CurrencyCode(578, "NOK", "¤", "no", "Norway", "Norwegian Krone"),
            CurrencyCode(524, "NPR", "¤", "np", "Nepal", "Nepalese Rupee"),
            CurrencyCode(554, "NZD", "¤", "nz", "New Zealand", "New Zealand Dollar"),
            CurrencyCode(512, "OMR", "¤", "om", "Oman", "Rial Omani"),
            CurrencyCode(590, "PAB", "¤", "pa", "Panama", "Balboa"),
            CurrencyCode(604, "PEN", "¤", "pe", "Peru", "Nuevo Sol"),
            CurrencyCode(598, "PGK", "¤", "pg", "Papua New Guinea", "Kina"),
            CurrencyCode(608, "PHP", "₱", "ph", "Philippines", "Philippine Peso"),
            CurrencyCode(586, "PKR", "¤", "pk", "Pakistan", "Pakistan Rupee"),
            CurrencyCode(985, "PLN", "¤", "pl", "Poland", "Zloty"),
            CurrencyCode(620, "PTE", "¤", "pt", "Portugal", "Portuguese Escudo"),
            CurrencyCode(600, "PYG", "¤", "py", "Paraguay", "Guarani"),
            CurrencyCode(634, "QAR", "¤", "qa", "Qatar", "Qatari Rial"),
            CurrencyCode(642, "ROL", "¤", "ro", "Romania", "Leu"),
            RUB,
            CurrencyCode(810, "RUR", "¤", "ru", "Russian Federation", "Russian Ruble"),
            CurrencyCode(646, "RWF", "¤", "rw", "Rwanda", "Rwanda Franc"),
            CurrencyCode(682, "SAR", "¤", "sa", "Saudi Arabia", "Saudi Riyal"),
            CurrencyCode(90, "SBD", "¤", "sb", "Solomon Islands", "Solomon Islands Dollar"),
            CurrencyCode(690, "SCR", "¤", "sc", "Seychelles", "Seychelles Rupee"),
            CurrencyCode(736, "SDD", "¤", "sd", "Sudan", "Sudanese Dinar"),
            CurrencyCode(752, "SEK", "¤", "se", "Sweden", "Swedish Krona"),
            CurrencyCode(702, "SGD", "¤", "sg", "Singapore", "Singapore Dollar"),
            CurrencyCode(705, "SIT", "¤", "si", "Slovenia", "Tolar"),
            CurrencyCode(703, "SKK", "¤", "si", "Slovakia", "Slovak Koruna"),
            CurrencyCode(694, "SLL", "¤", "sl", "Sierra Leone", "Leone"),
            CurrencyCode(706, "SOS", "¤", "so", "Somalia", "Somali Shilling"),
            CurrencyCode(740, "SRG", "¤", "sr", "Suriname", "Surinam Guilder"),
            CurrencyCode(678, "STD", "¤", "st", "Saint Tome and Principe", "Dobra"),
            CurrencyCode(222, "SVC", "¤", "sv", "El Salvador", "El Salvador Colon"),
            CurrencyCode(760, "SYP", "¤", "sy", "Syrian Arab Republic", "Syrian Pound"),
            CurrencyCode(748, "SZL", "¤", "sz", "Swaziland", "Lilangeni"),
            CurrencyCode(764, "THB", "?", "th", "Thailand", "Baht"),
            // CurrencyCode(762,"TJR","¤","tj","Tadjikistan (Old)","Tajik Ruble (old)"),
            CurrencyCode(972, "TJS", "¤", "tj", "Tadjikistan", "Somoni"),
            CurrencyCode(795, "TMM", "¤", "tm", "Turkmenistan", "Manat"),
            CurrencyCode(788, "TND", "¤", "tn", "Tunisia", "Tunisian Dinar"),
            CurrencyCode(776, "TOP", "¤", "to", "Tonga", "Pa'anga"),
            CurrencyCode(626, "TPE", "¤", "tp", "East Timor", "Timor Escudo"),
            CurrencyCode(792, "TRL", "£", "tr", "Turkey", "Turkish Lira"),
            CurrencyCode(780, "TTD", "¤", "tt", "Trinidad and Tobago", "Trinidad and Tobago Dollar"),
            CurrencyCode(901, "TWD", "¤", "tw", "Taiwan", "New Taiwan Dollar"),
            CurrencyCode(834, "TZS", "¤", "tz", "United Republic Of Tanzania", "Tanzanian Shilling"),
            CurrencyCode(980, "UAH", "¤", "ua", "Ukraine", "Hryvnia"),
            CurrencyCode(800, "UGX", "¤", "ug", "Uganda", "Uganda Shilling"),
            USD,
            CurrencyCode(858, "UYU", "¤", "uy", "Uruguay", "Peso Uruguayo"),
            CurrencyCode(860, "UZS", "¤", "uz", "Uzbekistan", "Uzbekistan Sum"),
            CurrencyCode(862, "VEB", "¤", "ve", "Venezuela", "Bolivar"),
            CurrencyCode(704, "VND", "¤", "vn", "Vietnam", "Dong"),
            CurrencyCode(548, "VUV", "¤", "vu", "Vanuatu", "Vatu"),
            CurrencyCode(882, "WST", "¤", "ws", "Samoa", "Tala"),
            CurrencyCode(950, "XAF", "¤", "ga", "Gabon/Guinea/Congo/Chad/Cameroon", "CFA Franc BEAC"),
            CurrencyCode(951, "XCD", "$", "gd", "Caribbean Islands", "Caribbean Dollar"),
            CurrencyCode(952, "XOF", "¤", "gw", "Guinea-Bissau/Togo/Senegal/niger/Mali", "CFA Franc BCEAO"),
            CurrencyCode(953, "XPF", "¤", "pf", "Polynesia/New Caledonia/Wallis", "CFP Franc"),
            CurrencyCode(999, "XXX", "", "", "", "No currency"),
            CurrencyCode(886, "YER", "¤", "ye", "Yemen", "Yemeni Rial"),
            CurrencyCode(891, "YUM", "¤", "yu", "Yugoslavia", "New Dinar"),
            CurrencyCode(710, "ZAR", "¤", "za", "South Africa/Namibia/Lesotho", "Rand"),
            CurrencyCode(894, "ZMK", "¤", "zm", "Zambia", "Kwacha"),
            // CurrencyCode(180,"ZRN","¤","zr","Zaire","New Zaire"),
            CurrencyCode(716, "ZWD", "$", "zw", "Zimbabwe", "Zimbabwe Dollar")
        )
    }

}