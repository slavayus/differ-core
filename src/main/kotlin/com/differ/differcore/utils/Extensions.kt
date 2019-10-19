package com.differ.differcore.utils

import java.util.*
import java.util.AbstractMap.SimpleEntry
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.collections.Map.Entry

fun String.isInt() = try {
    toInt()
    true
} catch (ex: NumberFormatException) {
    false
}

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

fun Map<*, *>.flatten(keySeparator: String): Map<String, Any> =
    entries
        .stream()
        .map { SimpleEntry<String, Any>(it.key.toString(), it.value) }
        .flatMap { flatten(it, keySeparator) }
        .collect(
            { LinkedHashMap() },
            { m, e -> m[keySeparator + e.key] = e.value },
            { obj, m -> obj.putAll(m) })

fun Map<*, *>.flatten(entry: Entry<String, Any>, keySeparator: String): Stream<Entry<String, Any>> =
    with(entry.value) {
        when (this) {
            is Map<*, *> ->
                entries
                    .stream()
                    .flatMap { e ->
                        flatten(
                            SimpleEntry<String, Any>(
                                "${entry.key}$keySeparator${e.key}",
                                nullAsString(e.value)
                            ),
                            keySeparator
                        )
                    }
            is List<*> ->
                IntStream.range(0, size)
                    .mapToObj { i -> SimpleEntry<String, Any>("${entry.key}$keySeparator-$i", nullAsString(this[i])) }
                    .flatMap { flatten(it, keySeparator) }
            else ->
                Stream.of(entry)
        }
    }

fun nullAsString(value: Any?): Any = value ?: "$value"

