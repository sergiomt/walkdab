package com.clocial.walkdab.app.util.str

import kotlin.math.min

/**
 * This file is licensed under the Apache License version 2.0.
 * You may not use this file except in compliance with the license.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.
 */
object Str {

    /**
     * Null safe comparison of two Strings
     * @param a String
     * @param b String
     * @return Boolean
     */
    fun nullSafeEq(a: String?, b: String?): Boolean = if (a == null && b == null) true else if (a == null && b != null) false else if (a != null && b == null) false else a == b

    /**
     * Ensure that a String ends with a given substring
     * @param sSource Input String
     * @param sEndsWith Substring that the String must end with.
     * @return If sSource ends with sEndsWith then sSource is returned,
     * else sSource+sEndsWith is returned.
     */
    fun chomp(sSource: String?, sEndsWith: String): String? {
        return if (null == sSource) null
        else if (sSource.length == 0) ""
        else if (sSource.endsWith(sEndsWith)) sSource
        else sSource + sEndsWith
    } // chomp

    /**
     * Ensure that a String does not end with a given character
     * @param sSource Input String
     * @param cEndsWith Character that the String must not end with.
     * @return If sSource does not end with sEndsWith then sSource is returned,
     * else sSource-cEndsWith is returned.
     */
    fun dechomp(sSource: String?, cEndsWith: Char): String? {
        return if (null == sSource) null
        else if (sSource.length < 1) sSource
        else if (sSource[sSource.length - 1] == cEndsWith) sSource.substring(0, sSource.length - 1)
        else sSource
    } // dechomp


    /**
     * Search for a String in an array
     * @param sStr String sought
     * @param aSet String[] Strings searched
     * @return boolean true if any String in set is equalsIgnoreCase to the given String.
     */
    fun inStr(sStr: String, aSet: Array<String?>?): Boolean {
        var bRetVal = false

        if (aSet != null) {
            val iLen = aSet.size

            var i = 0
            while (i < iLen && !bRetVal) {
                bRetVal = sStr.equals(aSet[i], ignoreCase = true)
                i++
            }
        } // fi


        return bRetVal
    }


    /**
     * Remove a character set from a String
     * @param sInput Input String
     * @param sRemove A String containing all the characters to be removed from input String
     * @return The input String without all the characters of sRemove
     */
    fun removeChars(sInput: String?, sRemove: String?): String? {
        if (null == sInput) return null
        if (null == sRemove) return sInput
        if (sInput.length == 0) return sInput
        if (sRemove.length == 0) return sInput

        val iLen = sInput.length
        val oOutput = StringBuffer(iLen)

        for (i in 0 until iLen) {
            val c = sInput[i]
            if (sRemove.indexOf(c) < 0) oOutput.append(c)
        } // next


        return oOutput.toString()
    } // removeChars

    /**
     * Get index of a substring inside another string
     * @param sSource String String to be scanned
     * @param sSought Substring to be sought
     * @param iStartAt int Index to start searching from
     * @return int Start index of substring or -1 if not found
     */
    fun indexOfIgnoreCase(sSource: String?, sSought: String?, iStartAt: Int): Int {
        if ((sSource == null) || (sSought == null)) return -1

        val iSrcLen = sSource.length
        val iSghLen = sSought.length

        if (iSrcLen < iSghLen) return -1

        if (iSrcLen == iSghLen) return (if (sSource.equals(sSought, ignoreCase = true)) 0 else -1)

        val iReducedLen = iSrcLen - iSghLen

        if (iStartAt + iSghLen > iSrcLen) return -1

        for (p in iStartAt until iReducedLen) {
            if (sSource.substring(p, p + iSghLen).equals(sSought, ignoreCase = true)) return p
        }
        return -1
    }


    /**
     * Get index of a substring inside another string
     * @param sSource String String to be scanned
     * @param sSought Substring to be sought
     * @return int Start index of substring or -1 if not found
     */
    fun indexOfIgnoreCase(sSource: String?, sSought: String?): Int {
        if ((sSource == null) || (sSought == null)) return -1

        val iSrcLen = sSource.length
        val iSghLen = sSought.length

        if (iSrcLen < iSghLen) return -1

        if (iSrcLen == iSghLen) return (if (sSource.equals(sSought, ignoreCase = true)) 0 else -1)

        val iReducedLen = iSrcLen - iSghLen

        for (p in 0 until iReducedLen) {
            if (sSource.substring(p, p + iSghLen).equals(sSought, ignoreCase = true)) return p
        }
        return -1
    } // indexOfIgnoreCase

    /**
     * Get substring after a given character sequence
     * @param sSource Source String
     * @param iFromIndex Index top start searching character sequence from
     * @param sSought Character sequence sought
     * @return Substring after sSought character sequence.
     * If source string is empty then return value is always an empty string.
     * If sought substring is empty then return value is the whole source string.
     * If sought substring is not found then return value is **null**
     * @throws StringIndexOutOfBoundsException
     * @throws NullPointerException is source or sought string is null
     * @since 4.0
     */
    @Throws(StringIndexOutOfBoundsException::class, NullPointerException::class)
    fun substrAfter(sSource: String, iFromIndex: Int, sSought: String): String? {
        var iFromIndex = iFromIndex
        val sRetVal: String?

        if (sSource.length == 0) {
            sRetVal = ""
        } else {
            if (sSought.length == 0) {
                sRetVal = sSource
            } else {
                iFromIndex = sSource.indexOf(sSought, iFromIndex)
                sRetVal = if (iFromIndex < 0) {
                    null
                } else {
                    if (iFromIndex == sSource.length - 1) {
                        ""
                    } else {
                        sSource.substring(iFromIndex + sSought.length)
                    }
                }
            }
        }
        return sRetVal
    } // substrAfter


    /**
     * Get substring from an index up to next given character
     * @param sSource Source String
     * @param iFromIndex Index top start searching character from
     * @param cSought Character sought
     * @return Substring between iFromIndex and cSought character
     * @throws StringIndexOutOfBoundsException if cSought character is not found at sSource
     * @since 4.0
     */
    @Throws(StringIndexOutOfBoundsException::class)
    fun substrUpTo(sSource: String?, iFromIndex: Int, cSought: Char): String? {
        val sRetVal: String?
        if (null == sSource) {
            sRetVal = null
        } else {
            val iToIndex = sSource.indexOf(cSought, iFromIndex)
            if (iToIndex < 0) throw StringIndexOutOfBoundsException("Gadgets.substrUpTo() character $cSought not found")
            sRetVal = if (iFromIndex == iToIndex) ""
            else sSource.substring(iFromIndex, iToIndex)
        }
        return sRetVal
    } // substrUpTo


    /**
     * Get substring from an index up to next given character sequence
     * @param sSource Source String
     * @param iFromIndex Index top start searching character from
     * @param sSought Character sequence sought
     * @return Substring between iFromIndex and sSought
     * @throws StringIndexOutOfBoundsException if sSought sequence is not found at sSource
     * @since 4.0
     */
    @Throws(StringIndexOutOfBoundsException::class)
    fun substrUpTo(sSource: String?, iFromIndex: Int, sSought: String): String {
        val sRetVal: String?
        if (null == sSource) {
            sRetVal = null
        } else {
            val iToIndex = sSource.indexOf(sSought, iFromIndex)
            if (iToIndex < 0) throw StringIndexOutOfBoundsException("Gadgets.substrUpTo() character $sSought not found")
            sRetVal = if (iFromIndex == iToIndex) ""
            else sSource.substring(iFromIndex, iToIndex)
        }
        return sRetVal!!
    } // substrUpTo


    /**
     * Get substring between two given character sequence
     * @param sSource Source String
     * @param sLowerBound Lower bound character sequence
     * @param sUpperBound Upper bound character sequence
     * @return Substring between sLowerBound and sUpperBound or **null** if
     * either sLowerBound or sUpperBound are not found at sSource
     * @since 4.0
     */
    @Throws(StringIndexOutOfBoundsException::class, NullPointerException::class)
    fun substrBetween(sSource: String, sLowerBound: String, sUpperBound: String): String? {
        var sRetVal = substrAfter(sSource, 0, sLowerBound)
        if (null != sRetVal) {
            sRetVal = if (sRetVal.indexOf(sUpperBound) >= 0) {
                substrUpTo(sRetVal, 0, sUpperBound)
            } else {
                null
            } // fi
        } // fi

        return sRetVal
    } // substrBetween

    /**
     * Get the leftmost characters of a String
     * @param sSource Source String
     * @param nChars Number of characters
     * @return String
     * @throws ArrayIndexOutOfBoundsException If nChars&lt;0
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun left(sSource: String?, nChars: Int): String? {
        if (sSource == null) return null
        if (nChars < 0) throw ArrayIndexOutOfBoundsException("Str.left() invalid maximum character length $nChars")
        return if (sSource.length <= nChars) sSource
        else sSource.substring(0, nChars)
    }

    /**
     * Add padding characters to the left.
     * @param sSource Input String
     * @param cPad Padding character
     * @param nChars Final length of the padded string
     * @return Padded String
     */
	@JvmStatic
	fun leftPad(sSource: String?, cPad: Char, nChars: Int): String? {
        if (null == sSource) return null

        val iPadLen = nChars - sSource.length

        if (iPadLen <= 0) return sSource

        val aPad = CharArray(iPadLen)

        aPad.fill(cPad)

        return String(aPad) + sSource
    } // leftPad

    // ----------------------------------------------------------
    /**
     *
     * Calculate Levenshtein distance between two strings
     * The Levenshtein distance is defined as the minimal number of characters
     * you have to replace, insert or delete to transform s into t.
     * The complexity of the algorithm is O(m*n),
     * where n and m are the length of s and t.
     * @param s String
     * @param t String
     * @return Levenshtein distance between s and t
     * @throws IllegalArgumentException if either s or t is **null**
     * @see [Michael Gilleland &amp; Chas Emerick](http://www.merriampark.com/ldjava.htm)
     *
     * @since 4.0
     */
    fun levenshteinDistance(s: String?, t: String?): Int {
        require(!(s == null || t == null)) { "Strings must not be null" }

        val n = s.length // length of s
        val m = t.length // length of t

        if (n == 0) {
            return m
        } else if (m == 0) {
            return n
        }

        var p = IntArray(n + 1) //'previous' cost array, horizontally
        var d = IntArray(n + 1) // cost array, horizontally
        var _d: IntArray //placeholder to assist in swapping p and d

        // indexes into strings s and t
        var i: Int // iterates through s

        var t_j: Char // jth character of t

        var cost: Int // cost

        i = 0
        while (i <= n) {
            p[i] = i
            i++
        }

        var j = 1 // iterates through t
        while (j <= m) {
            t_j = t[j - 1]
            d[0] = j

            i = 1
            while (i <= n) {
                cost = if (s[i - 1] == t_j) 0 else 1
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost				
                d[i] = min(
                    min((d[i - 1] + 1).toDouble(), (p[i] + 1).toDouble()),
                    (p[i - 1] + cost).toDouble()
                ).toInt()
                i++
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p
            p = d
            d = _d
            j++
        }


        // our last action in the above loop was to switch d and p, so p now 
        // actually has the most recent cost counts
        return p[n]
    } // getLevenshteinDistance

    // ----------------------------------------------------------
    /**
     * Convert each letter after space to Upper Case and all others to Lower Case
     * @param sSource Source String
     * @return Replaced string or **null** if sSource if **null**
     * @since 7.0
     */
    fun capitalizeFirst(sSource: String?): String? {
        if (null == sSource) {
            return null
        } else {
            val aChars = sSource.lowercase().toCharArray()
            val nChars = aChars.size
            var bFound = false
            for (i in 0 until nChars) {
                if (!bFound && Character.isLetter(aChars[i])) {
                    aChars[i] = aChars[i].uppercaseChar()
                    bFound = true
                } else if (Character.isWhitespace(aChars[i])) {
                    bFound = false
                }
            } // next

            return String(aChars)
        }
    } // capitalizeFirst

    // ----------------------------------------------------------
    /**
     * Convert each letter after space to Upper Case
     * if the word has exactly two characters then UpperCase also the second letter
     * convert all other letters to Lower Case
     * @param sSource Source String
     * @return Replaced string or **null** if sSource if **null**
     * @since 8.0
     */
    fun capitalizeFirstAndSecond(sSource: String?): String? {
        if (null == sSource) {
            return null
        } else {
            val aChars = sSource.lowercase().toCharArray()
            val nChars = aChars.size

            if (nChars <= 2) {
                if (nChars > 0) aChars[0] = aChars[0].uppercaseChar()
                if (nChars > 1) aChars[1] = aChars[1].uppercaseChar()
            } else {
                var bFound = false
                for (i in 0 until nChars) {
                    if (Character.isLetter(aChars[i])) {
                        if (!bFound) {
                            aChars[i] = aChars[i].uppercaseChar()
                            bFound = true
                        } else {
                            if (i < 2) {
                                if (Character.isWhitespace(aChars[i + 1])) aChars[i] = aChars[i].uppercaseChar()
                            } else { // i>=2
                                if (Character.isWhitespace(aChars[i - 2])) {
                                    if (i < nChars - 1) {
                                        if (Character.isWhitespace(aChars[i + 1])) aChars[i] = aChars[i].uppercaseChar()
                                    } else {
                                        aChars[i] = aChars[i].uppercaseChar()
                                    }
                                }
                            }
                        }
                    } else if (Character.isWhitespace(aChars[i])) {
                        bFound = false
                    }
                } // next      	
            }
            return String(aChars)
        }
    } // capitalizeFirstAndSecond

    // ----------------------------------------------------------

    /**
     * Remove double quotes, carriage returns, line feeds, STX and ETX characters
     */
    fun sanitizeBreaks(value: String?): String {
        return if (value.isNullOrEmpty()) {
            ""
        } else {
            val noBreaks = removeChars(value, "\"\r" + FF + STX + ETX)
            val noBreaksNonNull = noBreaks ?: ""
            noBreaksNonNull.replace('\n', ' ')
        }
    }

    // ----------------------------------------------------------

    private val two = 2
    private val three = 3
    private val twelve = 12

    val STX = two.toChar()
    val ETX = three.toChar()
    val FF = twelve.toChar()
}
