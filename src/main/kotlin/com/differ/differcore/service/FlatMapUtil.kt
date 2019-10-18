package com.differ.differcore.service

import java.util.*
import java.util.AbstractMap.SimpleEntry
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.collections.Map.Entry


class FlatMapUtil {
    companion object {
        fun flatten(map: Map<String, Any>): Map<String, Any> =
            map.entries
                .stream()
                .flatMap { flatten(it) }
                .collect(
                    { LinkedHashMap() },
                    { m, e -> m["." + e.key] = e.value },
                    { obj, m -> obj.putAll(m) })

        private fun flatten(entry: Entry<String, Any>): Stream<Entry<String, Any>> =
            with(entry.value) {
                when (this) {
                    is Map<*, *> ->
                        entries
                            .stream()
                            .flatMap { e -> flatten(SimpleEntry<String, Any>(entry.key + ".${e.key}", e.value)) }
                    is List<*> ->
                        IntStream.range(0, size)
                            .mapToObj { i -> SimpleEntry<String, Any>(entry.key + ".-$i", this[i]) }
                            .flatMap { flatten(it) }
                    else ->
                        Stream.of(entry)
                }
            }
    }
}