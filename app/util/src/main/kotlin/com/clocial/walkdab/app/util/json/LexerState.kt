package com.clocial.walkdab.app.util.json

enum class LexerState {
    VALUE,
    STRING_START,
    STR_ESC,
    NUMBER_START,
    ARRAY_START,
    T,
    TR,
    TRU,
    TRUE,
    F,
    FA,
    FAL,
    FALS,
    N,
    NU,
    NUL,
    AFTER_VALUE,
    HEX1,
    HEX2,
    HEX3,
    HEX4
}
