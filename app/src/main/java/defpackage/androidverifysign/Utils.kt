package defpackage.androidverifysign

import java.security.MessageDigest

fun ByteArray.toHex(): String = joinToString(":") {
    String.format("%02X", it)
}

fun ByteArray.hash(algorithm: String): String = MessageDigest.getInstance(algorithm).let {
    it.update(this)
    return it.digest().toHex()
}