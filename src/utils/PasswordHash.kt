package linhdo.backend.inface.utils

import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@KtorExperimentalAPI
object PasswordHash {
    private val hashKey = hex("6819b57a326945c1968f45236589")

    private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

    fun hash(password: String): String {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(hmacKey)
        return hex(hmac.doFinal(password.toByteArray()))
    }
}
