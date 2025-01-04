package com.clocial.walkdab.crypto

import kotlin.random.Random
import kotlin.system.measureTimeMillis

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Assertions.assertArrayEquals

class CryptographyServiceImplTest {

@Test
fun testCryptographyServiceImplEncryptDecryptWithPassword() {
    val srvc = CryptographyServiceFactory.Default
    val psswrd = "S€cr€t1P@sswrd"
    val dataToEncrypt = byteArrayOf(34,87,90,78,88,56,34,0,0,0,81,11,78,98,14,16,17,18,19,20,100,111,0,125,2,49,86,41)
    val encryptedBytes = srvc.encryptWithPasword(dataToEncrypt, psswrd)
    val decryptedBytes = srvc.decryptWithPassword(encryptedBytes, psswrd)
    assertArrayEquals(dataToEncrypt, decryptedBytes)
    }

@Disabled
@Test
fun testCryptographyServiceImplEncryptDecryptHeavyLoad() {
    val srvc = CryptographyServiceFactory.Default
    val psswrd = "S€cr€t1P@sswrd"
    val dataToEncrypt = ByteArray(50*10124*1024) // 50Mb array
    var encryptedBytes: ByteArray
    var decryptedBytes: ByteArray
    Random.nextBytes(dataToEncrypt)
    val timeToEncrypt = measureTimeMillis { encryptedBytes = srvc.encryptWithPasword(dataToEncrypt, psswrd) }
    val timeToDecrypt = measureTimeMillis { decryptedBytes =srvc.decryptWithPassword(encryptedBytes, psswrd) }
    assertArrayEquals(dataToEncrypt, decryptedBytes)
    print("Time taken to encrypt 50Mb $timeToEncrypt millis")
    print("Time taken to decrypt 50Mb $timeToDecrypt millis")
    }

}

