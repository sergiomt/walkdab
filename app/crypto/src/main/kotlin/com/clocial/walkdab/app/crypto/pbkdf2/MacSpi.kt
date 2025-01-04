package com.clocial.walkdab.app.crypto.pbkdf2

abstract class MacSpi : Cloneable {

    protected abstract fun engineGetMacLength(): Int

    protected abstract fun engineInit(secret: ByteArray)

    protected abstract fun engineUpdate(var1: Byte)

    protected abstract fun engineUpdate(input: ByteArray, offset: Int, length: Int)

    protected abstract fun engineDoFinal(): ByteArray?

    protected abstract fun engineReset()

}
