package com.differ.differcore.service

import com.differ.differcore.utils.*
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.math.absoluteValue

@Service
open class MapTransformerServiceImpl : MapTransformerService {

    override fun flattenMap(mapToFlatten: Map<String, Any>): Map<String, Any?> =
        mapToFlatten.entries
            .stream()
            .map { it.key to it.value }
            .flatMap { flatten(it, JSON_KEY_SEPARATOR) }
            .collect(
                { LinkedHashMap() },
                { map, entry -> map[JSON_KEY_SEPARATOR + entry.first] = entry.second },
                { map, m -> map.putAll(m) })

    private fun flatten(entry: Pair<String, Any?>, keySeparator: String): Stream<Pair<String, Any?>> =
        with(entry.second) {
            when (this) {
                is Map<*, *> ->
                    entries.stream()
                        .flatMap { e -> flatten("${entry.first}$keySeparator${e.key}" to e.value, keySeparator) }
                is List<*> ->
                    IntStream.range(0, size)
                        .mapToObj { i -> "${entry.first}$keySeparator-$i" to this[i] }
                        .flatMap { flatten(it, keySeparator) }
                else ->
                    Stream.of(entry)
            }
        }


    override fun expandToMapObjects(flattenMap: Map<String, Any?>) =
        on(mutableMapOf<String, Any?>()) {
            flattenMap.entries.forEach { addEntry(it, this) }
        }


    private fun addEntry(entry: Map.Entry<String, Any?>, jsonMap: MutableMap<String, Any?>) {
        val keyList = entry.key.split(JSON_KEY_SEPARATOR)
        val key = keyList[1]
        val value = jsonMap[key]
        val secondKey = secondKey(keyList)
        var remainKey = joinKeyList(keyList, 2)
        var deeperMap = mutableMapOf<String, Any?>()
        when {
            value is MutableMap<*, *> -> deeperMap = value.asMutableMapOfType()!!

            keyList.size <= 3 && secondKey.isNegativeNumber() -> {
                val list = if (value is MutableList<*>) value else mutableListOf<Any>()
                populateList(list, entry.value)
                jsonMap[key] = list
                return
            }

            secondKey.isNegativeNumber() -> {
                jsonMap.putIfAbsent(key, mutableListOf(deeperMap))
                deeperMap = getExistingMapOrCreateNew(jsonMap[key] as MutableList<*>, secondKey)
                remainKey = joinKeyList(keyList, 3)
            }

            secondKey.isEmpty() -> {
                jsonMap[key] = entry.value
                return
            }

            else -> jsonMap[key] = deeperMap
        }
        addEntry(AbstractMap.SimpleEntry(remainKey, entry.value), deeperMap)
    }

    private fun getExistingMapOrCreateNew(value: MutableList<*>, secondKey: String): MutableMap<String, Any?> =
        when {
            value.size > secondKey.toInt().absoluteValue ->
                (value[secondKey.toInt().absoluteValue] as MutableMap<*, *>).asMutableMapOfType()!!
            else -> {
                val result = mutableMapOf<String, Any?>()
                populateList(value, result)
                result
            }
        }

    private fun populateList(list: MutableList<*>, value: Any?) = list.run { asMutableListOfType<Any?>()?.add(value) }

    private fun joinKeyList(keyList: List<String>, start: Int) =
        keyList.subList(start).joinToString(JSON_KEY_SEPARATOR, JSON_KEY_SEPARATOR)

    private fun secondKey(keyList: List<String>) = takeIf { (keyList.size > 2) }?.let { keyList[2] } ?: ""

    companion object {
        private const val JSON_KEY_SEPARATOR = "."
    }
}