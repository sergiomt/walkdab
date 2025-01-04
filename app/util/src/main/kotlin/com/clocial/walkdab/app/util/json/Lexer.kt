package com.clocial.walkdab.app.util.json

import java.math.BigDecimal

class Lexer {

    abstract class CB {
        // Implement these if you wish to write your own Parser, or
        // use the JSON class, which is also a good place to look at
        // an example of how to extend CB
        abstract fun tok(tok: LexerToken)
        abstract fun tok(s: String?)
        abstract fun tok(s: BigDecimal?)

        // these are to for internal use only, for the lexer to
        // maintain state between calls.
        // don't touch :)
        var pos: Int = 0
        var state: LexerState = LexerState.VALUE
        var cache: StringBuilder? = null
        var hexCache = StringBuilder()
    }

    fun lex(arr: ByteArray, cb: CB) {
        lex(arr, 0, arr.size, cb)
    }

    fun lex(arr: ByteArray, off: Int, len: Int, cb: CB) {
        val t = 12
        val ff = t.toChar()
        var i = off
        val end = off + len
        while (i != end) {
            val c = Char(arr[i].toUShort())
            when (cb.state) {
                LexerState.VALUE -> {
                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '"' -> {
                            cb.state = LexerState.STRING_START
                            cb.cache = StringBuilder()
                            ++i
                            ++cb.pos
                            continue
                        }

                        '-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' -> {
                            cb.state = LexerState.NUMBER_START
                            cb.cache = StringBuilder()
                            cb.cache!!.append(c)
                            ++i
                            ++cb.pos
                            continue
                        }

                        '{' -> {
                            cb.state = LexerState.VALUE
                            cb.tok(LexerToken.LCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        '}' -> {
                            cb.state = LexerState.AFTER_VALUE
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        '[' -> {
                            cb.state = LexerState.VALUE
                            cb.tok(LexerToken.LSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.state = LexerState.AFTER_VALUE
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        't' -> {
                            cb.state = LexerState.T
                            ++i
                            ++cb.pos
                            continue
                        }

                        'f' -> {
                            cb.state = LexerState.F
                            ++i
                            ++cb.pos
                            continue
                        }

                        'n' -> {
                            cb.state = LexerState.N
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> error(cb, c)
                    }
                    if ('r' == c) {
                        cb.state = LexerState.TR
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('u' == c) {
                        cb.state = LexerState.TRU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.TRUE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('a' == c) {
                        cb.state = LexerState.FA
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.FAL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('s' == c) {
                        cb.state = LexerState.FALS
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.FALSE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.T -> {
                    if ('r' == c) {
                        cb.state = LexerState.TR
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('u' == c) {
                        cb.state = LexerState.TRU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.TRUE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('a' == c) {
                        cb.state = LexerState.FA
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.FAL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('s' == c) {
                        cb.state = LexerState.FALS
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.FALSE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.TR -> {
                    if ('u' == c) {
                        cb.state = LexerState.TRU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.TRUE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('a' == c) {
                        cb.state = LexerState.FA
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.FAL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('s' == c) {
                        cb.state = LexerState.FALS
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.FALSE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.TRU -> {
                    if ('e' == c) {
                        cb.tok(LexerToken.TRUE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('a' == c) {
                        cb.state = LexerState.FA
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.FAL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('s' == c) {
                        cb.state = LexerState.FALS
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.FALSE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.F -> {
                    if ('a' == c) {
                        cb.state = LexerState.FA
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.FAL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('s' == c) {
                        cb.state = LexerState.FALS
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.FALSE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.FA -> {
                    if ('l' == c) {
                        cb.state = LexerState.FAL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('s' == c) {
                        cb.state = LexerState.FALS
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.FALSE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.FAL -> {
                    if ('s' == c) {
                        cb.state = LexerState.FALS
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('e' == c) {
                        cb.tok(LexerToken.FALSE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.FALS -> {
                    if ('e' == c) {
                        cb.tok(LexerToken.FALSE)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.N -> {
                    if ('u' == c) {
                        cb.state = LexerState.NU
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.NU -> {
                    if ('l' == c) {
                        cb.state = LexerState.NUL
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.NUL -> {
                    if ('l' == c) {
                        cb.tok(LexerToken.NULL)
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                    error(cb, c)

                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.AFTER_VALUE -> {
                    if (isWS(c)) {
                        ++i
                        ++cb.pos
                        continue
                    }
                    when (c) {
                        '}' -> {
                            cb.tok(LexerToken.RCURLY)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ']' -> {
                            cb.tok(LexerToken.RSQUARE)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ',' -> {
                            cb.tok(LexerToken.COMMA)
                            ++i
                            ++cb.pos
                            continue
                        }

                        ':' -> {
                            cb.tok(LexerToken.COLON)
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> {
                            --i
                            --cb.pos
                            cb.state = LexerState.VALUE
                            ++i
                            ++cb.pos
                            continue
                        }
                    }
                }

                LexerState.NUMBER_START -> when (c) {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'e', 'E', '+', '-', '.' -> {
                        cb.cache!!.append(c)
                        ++i
                        ++cb.pos
                        continue
                    }

                    else -> {
                        cb.tok(num(if (cb.cache==null) "" else cb.cache.toString()))
                        --i
                        --cb.pos
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }
                }

                LexerState.STRING_START -> when (c) {
                    '"' -> {
                        cb.tok(cb.cache.toString())
                        cb.state = LexerState.AFTER_VALUE
                        ++i
                        ++cb.pos
                        continue
                    }

                    '\\' -> {
                        cb.state = LexerState.STR_ESC
                        ++i
                        ++cb.pos
                        continue
                    }

                    else -> {
                        if (Character.isISOControl(c)) {
                            error(cb, c)
                        }
                        cb.cache!!.append(c)
                        ++i
                        ++cb.pos
                        continue
                    }
                }

                LexerState.STR_ESC -> {
                    when (c) {
                        '"', '/', '\\' -> cb.cache!!.append(c)
                        'b' -> cb.cache!!.append('\b')
                        'f' -> cb.cache!!.append(ff)
                        'n', 'r' -> cb.cache!!.append('\r')
                        't' -> cb.cache!!.append('\t')
                        'u' -> {
                            cb.state = LexerState.HEX1
                            ++i
                            ++cb.pos
                            continue
                        }

                        else -> error(cb, c)
                    }
                    cb.state = LexerState.STRING_START
                    ++i
                    ++cb.pos
                    continue
                }

                LexerState.HEX1 -> {
                    if (!isHex(c)) {
                        error(cb, c)
                    }
                    cb.hexCache = StringBuilder()
                    cb.hexCache.append(c)
                    cb.state = LexerState.HEX2
                    ++i
                    ++cb.pos
                    continue
                }

                LexerState.HEX2 -> {
                    if (!isHex(c)) {
                        error(cb, c)
                    }
                    cb.hexCache.append(c)
                    cb.state = LexerState.HEX3
                    ++i
                    ++cb.pos
                    continue
                }

                LexerState.HEX3 -> {
                    if (!isHex(c)) {
                        error(cb, c)
                    }
                    cb.hexCache.append(c)
                    cb.state = LexerState.HEX4
                    ++i
                    ++cb.pos
                    continue
                }

                LexerState.HEX4 -> {
                    if (!isHex(c)) {
                        error(cb, c)
                    }
                    cb.hexCache.append(c)
                    val u = toChar(cb.hexCache)
                    cb.cache!!.append(u)
                    cb.state = LexerState.STRING_START
                    ++i
                    ++cb.pos
                    continue
                }

                else -> error(cb, c)
            }
            ++i
            ++cb.pos
        }
    }

    fun isWS(c: Char): Boolean {
        return Character.isWhitespace(c)
    }

    fun toChar(buf: CharSequence): Char {
        assert(buf.length == 4)
        return buf.toString().toInt(16).toChar()
    }

    fun num(b: String?): BigDecimal? {
        if (null==b) return null
        if (b.isEmpty()) return null
        var bd: BigDecimal? = null
        try {
            bd = BigDecimal(b)
        } catch (t: Throwable) {
            error("not a number: $b")
        }
        return bd
    }

    fun isHex(c: Char): Boolean {
        return when (c) {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F' -> true
            else -> false
        }
    }

    fun error(cb: CB) {
        error("??? " + cb.state + " at pos: " + cb.pos)
    }

    fun error(cb: CB, c: Char) {
        error("unexpected char: " + c + "(" + c + ") in state: " + cb.state + " at pos:" + cb.pos)
    }

    fun error(mes: String?) {
        throw RuntimeException(mes)
    }

}
