package com.differ.differcore.service

import com.differ.differcore.utils.isInt
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.Maps
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import kotlin.math.absoluteValue


@Service
class DiffService {
    private val jsonKeyDelimiter = "."
    fun diff() {
        mapDifferenceGuava()
    }

    private fun mapDifferenceGuava() {
        val mapper = ObjectMapper()
        val type = object : TypeReference<Map<String, Any>>() {}

        val leftMap =
            mapper.readValue<Map<String, Any>>(
                File("/Users/iusiumbeli/univer/diplom/differ-core/src/main/resources/jversions/0001.json"),
                type
            )
        val rightMap =
            mapper.readValue<Map<String, Any>>(
                File("/Users/iusiumbeli/univer/diplom/differ-core/src/main/resources/jversions/0002.json"),
                type
            )
        val leftFlatMap = FlatMapUtil.flatten(leftMap)
        val rightFlatMap = FlatMapUtil.flatten(rightMap)

        val difference = Maps.difference<String, Any>(leftFlatMap, rightFlatMap)

        var outString = ""
        outString += "Entries only on left\n--------------------------\n"
        difference.entriesOnlyOnLeft().forEach { (key, value) -> outString += "$key: $value\n" }

        outString += "\n\nEntries only on right\n--------------------------\n"
        difference.entriesOnlyOnRight().forEach { (key, value) -> outString += "$key: $value\n" }

        outString += "\n\nEntries differing\n--------------------------\n"
        difference.entriesDiffering().forEach { (key, value) -> outString += "$key: $value\n" }

        outString += "\n\nEntries in common\n--------------------------\n"
        difference.entriesInCommon().forEach { (key, value) -> outString += "$key: $value\n" }
        val outFile = File("out-diff.txt")
        outFile.writeText(outString)

        expandToJson(with(mutableMapOf<String, Any>()) {
            putAll(difference.entriesOnlyOnLeft())
            putAll(difference.entriesOnlyOnRight())
            putAll(difference.entriesDiffering())
            this
        }.toMap())


    }

    fun expandToJson(unionData: Map<String, Any>): MutableMap<String, Any> {
        val jsonMap = mutableMapOf<String, Any>()
        unionData.entries.stream()
            .forEach { addEntry(it, jsonMap) }
        return jsonMap
    }

    fun addEntry(entry: Map.Entry<String, Any>, jsonMap: MutableMap<String, Any>) {
        val key = entry.key.run { if (length > 0) substring(1) else this }
            .run { if (entry.key.contains(jsonKeyDelimiter)) substringBefore(jsonKeyDelimiter) else this }
        val value = jsonMap[key]
        if (value != null) {
            when (value) {
                is MutableMap<*, *> ->
                    addEntry(
                        AbstractMap.SimpleEntry<String, Any>(
                            entry.key.removePrefix("$jsonKeyDelimiter$key"),
                            entry.value
                        ), value as MutableMap<String, Any>
                    )
                is MutableList<*> -> {
                    val secondKey = if (key.isNotEmpty()) {
                        entry.key.substring(key.length + 1).run { if (length > 0) substring(1) else this }
                            .run { if (entry.key.contains(jsonKeyDelimiter)) substringBefore(jsonKeyDelimiter) else this }
                    } else {
                        ""
                    }
                    var currentMap = mutableMapOf<String, Any>()
                    var newKey = entry.key.removePrefix("$jsonKeyDelimiter$key")
                    if (newKey.removePrefix("$jsonKeyDelimiter$secondKey").isEmpty() && (secondKey.isInt() && secondKey.toInt() <= 0)) {
                        (value as MutableList<String>).add(entry.value as String)
                        return
                    } else if (secondKey.isInt() && secondKey.toInt() <= 0) {
                        if (value.size > secondKey.toInt().absoluteValue) {
                            currentMap = value[secondKey.toInt().absoluteValue] as MutableMap<String, Any>
                        } else {
                            (value as MutableList<MutableMap<String, Any>>).add(currentMap)
                        }
                        newKey = newKey.removePrefix("$jsonKeyDelimiter$secondKey")
                    } else if (secondKey.isEmpty()) {
                        jsonMap[key] = entry.value
                        return
                    } else {
                        jsonMap[key] = currentMap
                    }
                    addEntry(
                        AbstractMap.SimpleEntry<String, Any>(
                            newKey,
                            entry.value
                        ), currentMap
                    )
                }
            }
        } else {
            val secondKey = if (key.isNotEmpty()) {
                entry.key.substring(key.length + 1).run { if (length > 0) substring(1) else this }
                    .run { if (entry.key.contains(jsonKeyDelimiter)) substringBefore(jsonKeyDelimiter) else this }
            } else {
                ""
            }
            val newMap = mutableMapOf<String, Any>()
            var newKey = entry.key.removePrefix("$jsonKeyDelimiter$key")
            if (newKey.removePrefix("$jsonKeyDelimiter$secondKey").isEmpty() && (secondKey.isInt() && secondKey.toInt() <= 0)) {
                jsonMap[key] = mutableListOf<String>().apply { add(entry.value as String) }
                return
            } else if (secondKey.isInt() && secondKey.toInt() <= 0) {
                jsonMap[key] = mutableListOf<MutableMap<String, Any>>().apply { add(newMap) }
                newKey = newKey.removePrefix("$jsonKeyDelimiter$secondKey")
            } else if (secondKey.isEmpty()) {
                jsonMap[key] = entry.value
                return
            } else {
                jsonMap[key] = newMap
            }
            addEntry(
                AbstractMap.SimpleEntry<String, Any>(
                    newKey,
                    entry.value
                ), newMap
            )
        }
    }
}