package com.differ.differcore.utils

/**
 * Checks is this string is [Int] or not.
 *
 * @receiver [String]
 *
 * @return `true` is this string is [Int], `false` otherwise.
 */
fun String.isInt() = toIntOrNull() != null

/**
 * Checks is this string is [Int] and negative number or not.
 *
 * @receiver [String]
 *
 * @return `true` is this string is [Int] and negative number, `false` otherwise.
 */
fun String.isNegativeNumber() = isInt() && toInt() <= 0

/**
 * Cast [MutableList] with raw parameter type to specified parameter `T`.
 *
 * Checks is this [MutableList] contains elements of specified parameter `T` type.
 * If this is `true` then receiver are casted to [MutableList] with specified `T` type.
 *
 * @receiver [MutableList] with raw type.
 *
 * @return casted current [MutableList] with raw type to [MutableList] with `T` type, `null` otherwise.
 */
inline fun <reified T> MutableList<*>.asMutableListOfType(): MutableList<T>? =
    if (all { it is T })
        @Suppress("UNCHECKED_CAST")
        this as MutableList<T> else
        null

/**
 * Cast [MutableMap] with raw type to specified `K` and `V` types.
 *
 * Checks is this [MutableMap] contains elements of specified `K` and `V` types.
 * If this is `true` then receiver are casted to [MutableMap] with `K` and `V` types.
 *
 * @receiver [MutableMap] with raw types.
 *
 * @return casted current [MutableMap] with raw type to [MutableMap] with `K` and `V` types, `null` otherwise.
 */
inline fun <reified K, reified V> MutableMap<*, *>.asMutableMapOfType(): MutableMap<K, V>? =
    if (all { it.key is K && it.value is V })
        @Suppress("UNCHECKED_CAST")
        this as MutableMap<K, V> else
        null

/**
 * Calls the specified function [block] with the given [receiver] as its receiver and returns [receiver].
 */
inline fun <T, R> on(receiver: T, block: T.() -> R): T {
    receiver.block()
    return receiver
}