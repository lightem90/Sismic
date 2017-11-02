package com.polito.sismic.Interactors.Helpers

import android.util.Base64
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


/**
 * Created by Matteo on 14/10/2017.
 */
class AESCryptHelper {

    companion object {

        private val ALGORITHM = "AES/CBC/PKCS5Padding"
        private val KEY = "seismicsverifies"

        fun encrypt(value: String): String {
            val key = generateKey()
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val encryptedByteValue = cipher.doFinal(value.toByteArray(charset("utf-8")))
            return Base64.encodeToString(encryptedByteValue, Base64.DEFAULT)

        }

        fun decrypt(value: String): String {
            val key = generateKey()
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, key)
            val decryptedValue64 = Base64.decode(value, Base64.DEFAULT)
            val decryptedByteValue = cipher.doFinal(decryptedValue64)
            return String(decryptedByteValue, Charset.defaultCharset())     //utf-8

        }

        private fun generateKey(): SecretKeySpec {
            return SecretKeySpec(AESCryptHelper.KEY.toByteArray(), AESCryptHelper.ALGORITHM)
        }
    }

}