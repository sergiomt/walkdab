package com.clocial.walkdab.app.util.json

import com.clocial.walkdab.app.util.json.Lexer.CB

import java.math.BigDecimal

import kotlin.collections.ArrayDeque

/** Simple JSON parsing for Java.  This class provides a rudimentary
 * callback implementation that may be passed to @see json.Lexer . In
 * case you are interested in writing your own specialized callback to
 * use with json.Lexer the callback contained herein may be a good
 * starting point.
 *
 * JSON objects (`{"bla":1}`) are converted to `java.utils.Map's` (Maps
 * in the interface HashMaps for the implementation), JSON arrrays are
 * converted to `java.util.List's` (LinkedList for the implementation),
 * JSON Strings become Java Strings, Numbers become `BigDecimal`, `true`
 * and `false` are boolean and null is, well, null.
 *
 * <h2> Usage </h2> <code>
 *
 * <code>
 *   String json = "{\"a\":19560954609845.4456456,\"b\":[1,2,3],\"dindong\":{\"b\":12}}";
 *   Object    o = JSON.parseJSON(json);
 * </code>
 *
 * In the example above, `o` will be a `java.util.Map` containing three
 * keys, "a", "b" and "dingdong", each Strings. "a"'s value is a
 * BigDecimal, "b"'s an array containing the BigDecimal values 1, 2,
 * and 3 and "dingdong"'s value is another Map ...
 *
 * The intended use case for this is to write non-blocking webservices,
 * this interface is meant to provide the functionality to process any
 * scrap of data that happens to be available on the network. This
 * requires a slightly more elaborate interface than the simple verson
 * above:
 *
 *   <code>
 *  		JSON json = new JSON();
 *  		while (arr = bytesAvailableFromSomewhere()) {
 *  			j.parse(arr);
 *  			if (json.done()) break;
 *  		}
 *  		Object result = json.obj();
 *   </code>
 *
 * <h2> Accepted JSON </h2>
 *
 * This implementation should be able to handle any JSON conforming to
 * JSON as described here (http://json.org).
 *
 * Technically, this parser accepts a superset of JSON that allows
 * redundant ':' and ',' inside of JSON objects and Arrays to be left
 * out, so it would accept:
 *
 *   <code>
 *     { "a" 19560954609845.4456456 "b" [1 2 3] "dindong" {"b" 12}}
 *   </code>
 *
 * as the equivalent of:
 *
 *   <code>
 *     { "a" : 19560954609845.4456456, "b" : [1 2 3], "dindong" : {"b" 12}}
 *   </code>
 *
 */

class JSON {
    class LexerCB : CB() {
        var stack: ArrayDeque<Any> = ArrayDeque()
        var done: Boolean = false
        var expectNextCommaOrRCurly: Boolean = false

        override fun tok(t: LexerToken) {
            if (done) {
                error()
            }
            if (expectNextCommaOrRCurly) {
                when (t) {
                    LexerToken.RCURLY, LexerToken.COMMA -> expectNextCommaOrRCurly = false
                    else -> error("unbalanced key value pairs")
                }
            }
            when (t) {
                LexerToken.LCURLY -> stack.add(map())
                LexerToken.LSQUARE -> stack.add(list())
                LexerToken.RCURLY -> {
                    val lastElement = stack.last()
                    if (lastElement !is Map<*, *>) {
                        error("misplaced }")
                    }
                    stack.removeLast()
                    stash(lastElement)
                }

                LexerToken.RSQUARE -> {
                    val lastElement = stack.last()
                    if (lastElement !is List<*>) {
                        error("misplaced ]")
                    }
                    stack.removeLast()
                    stash(lastElement)
                }

                LexerToken.TRUE -> stash(true)
                LexerToken.FALSE -> stash(false)
                LexerToken.NULL -> stash(NullValue())
                LexerToken.COMMA -> {}
                LexerToken.COLON -> if (stack.last() !is Key) {
                    error("misplaced :")
                }
            }
        }

        override fun tok(s: String?) {
            if (done) {
                error()
            }
            val lastElement = stack.last()
            if (lastElement is Map<*, *>) {
                stack.add(Key(s))
            } else {
                stash(s ?: NullValue())
            }
        }

        override fun tok(d: BigDecimal?) {
            stash(d ?: NullValue())
        }

        fun pop(): Any {
			val lastElement = stack.last()
			stack.removeLast()
			return lastElement
        }

        fun stash(o: Any) {
            // stack is empty, done
            if (0 == stack.size) {
                done = true
                stack.add(o)
                return
            }
            val top = stack.last()
            if (top is List<*>) {
                (top as MutableList<Any?>).add(o)
            } else if (top is Key) {
                val key = pop() as Key
                assert(stack.size > 0)
                assert(stack.last() is Map<*, *>)
                (stack.last() as MutableMap<String?, Any?>?)!![key.s] = o
                expectNextCommaOrRCurly = true
            } else {
                error("unexpected: " + o.javaClass.name + " after: " + (stack.last().javaClass.name))
            }
        }

        fun map(): Map<*, *> {
            return HashMap<Any?, Any?>()
        }

        fun list(): MutableList<Any> {
            return mutableListOf()
        }

        @JvmOverloads
        fun error(m: String? = "?") {
            throw RuntimeException(m)
        }
    }

    internal class Key (var s: String?)
    // Internal Marker class to keep track of keys and values on the
    // stack of the parser. A `Key` object may only be placed on top of
    // a `Map` (JSON Object). Encountering a `COLON` should only happen
    // when there is a `Key` on top of the stack, etc.

    var cb: LexerCB = LexerCB()
    var obj: Any? = null // result.

    /**
     * Parse whatever bits of JSON you have available to you.
     */
    @JvmOverloads
    fun parse(arr: ByteArray, off: Int = 0, len: Int = arr.size) {
        lexer.lex(arr, off, len, this.cb)
    }

    /**
     * Returns whether the parser is in a consistant, balanced state.
     * Once the parser is `done` passing further data to it via `parse`
     * will trigger an Exception.
     */
    private fun done(): Boolean {
        return cb.done
    }

    /**
     * Retrieve the results of the parse. You need to ensure that the
     * complete JSON object has been passed to parse, else this will throw
     * and Exception. Ideally, call `done()` before trying to retrieve the
     * results
     */
    fun obj(): Any {
        if (!done()) {
            throw RuntimeException("not done!")
        }
        if (null == obj) {
            val popedObj = cb.pop()
            obj = popedObj
            return popedObj
        } else {
            return obj ?: NullValue()
        }
    }

    companion object {
        /**
         * Utility method to parse a String containing valid JSON
         */

        private val lexer = Lexer()

        fun parse(json: String): Any {
            val cb = LexerCB()
            lexer.lex(json.toByteArray(), cb)
            return cb.pop()
        }

        /**
         * Mungle up an Object into JSON. There are a bunch of
         * cases this can't handle, for example: just any old stuff.
         *
         * Object passed to this method needs to be:
         * <ul>
         * <li> primitive
         * <li> Map
         * <li> List
         * <li> an Array of one of the above
         * </ul>
         */
        fun jsonify(o: Any): String {
            val e = Encoder()
            e.encode(o)
            return e.buf.toString()
        }

        fun jsonifyCustom(o: Any, enc: CustomEncoder): String {
            enc.encodeCustom(o)
            return enc.buf.toString()
        }
    }
}
