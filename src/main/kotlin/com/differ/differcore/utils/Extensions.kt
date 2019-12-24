package com.differ.differcore.utils

fun String.isInt() = try {
    toInt()
    true
} catch (ex: NumberFormatException) {
    false
}

fun String.isNegativeNumber() = isInt() && toInt() <= 0

inline fun <reified T> MutableList<*>.asMutableListOfType(): MutableList<T>? =
    if (all { it is T })
        @Suppress("UNCHECKED_CAST")
        this as MutableList<T> else
        null

inline fun <reified T> MutableList<*>.add(): MutableList<T>? =
    if (all { it is T })
        @Suppress("UNCHECKED_CAST")
        this as MutableList<T> else
        null

inline fun <reified K, reified V> MutableMap<*, *>.asMutableMapOfType(): MutableMap<K, V>? =
    if (all { it.key is K && it.value is V })
        @Suppress("UNCHECKED_CAST")
        this as MutableMap<K, V> else
        null

inline fun <reified T> List<T>.subList(start: Int): List<T> =
    subList(start, size)

/**
 * Вызывает указанную функцию [block] с указанным [receiver] в качестве получателя и возвращает [receiver].
 */
inline fun <T, R> on(receiver: T, block: T.() -> R): T {
    receiver.block()
    return receiver
}