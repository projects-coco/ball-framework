package org.coco.core.utils

import java.security.SecureRandom
import kotlin.random.asKotlinRandom

private object Base32Constants {
    const val BASE_32 = "0123456789ABCDEFGHJKMNPQRSTVWXYZ"
    val random = SecureRandom().asKotlinRandom()
}

fun String.Companion.newBase32Random(length: Int = 10): String =
    (1..length).map { Base32Constants.BASE_32.random(Base32Constants.random) }.joinToString("")

fun String.camelToSnake(): String {
    val pattern = "(?<=.)[A-Z]".toRegex()
    return this.replace(pattern, "_$0").lowercase()
}

fun String.snakeToCamel(): String {
    val pattern = "_[a-z]".toRegex()
    return replace(pattern) { it.value.last().uppercase() }
}
