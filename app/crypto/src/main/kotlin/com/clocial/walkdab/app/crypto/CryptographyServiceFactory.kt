package com.clocial.walkdab.crypto

import com.clocial.walkdab.app.crypto.impl.CryptographyServiceImpl

class CryptographyServiceFactory {

    companion object {
        val Default = CryptographyServiceImpl()
    }

}