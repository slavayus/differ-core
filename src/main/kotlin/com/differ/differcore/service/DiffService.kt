package com.differ.differcore.service

import com.differ.differcore.utils.asMutableListOfType
import com.differ.differcore.utils.asMutableMapOfType
import com.differ.differcore.utils.isInt
import com.differ.differcore.utils.subList
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

    private fun addEntry(entry: Map.Entry<String, Any>, jsonMap: MutableMap<String, Any>) {
        val keyList = entry.key.split(jsonKeyDelimiter)
        val key = keyList[1]
        val value = jsonMap[key]
        val secondKey = secondKey(keyList)
        var tmpMap = mutableMapOf<String, Any>()
        var leftKey = newKey(keyList, 2)
        if (value != null) {
            when (value) {
                is MutableMap<*, *> -> value.asMutableMapOfType<String, Any>()?.let {
                    addEntry(AbstractMap.SimpleEntry<String, Any>(newKey(keyList, 2), entry.value), it)
                }
                is MutableList<*> -> {
                    if (keyList.size <= 3 && (secondKey.isInt() && secondKey.toInt() <= 0)) {
                        populateList(value, entry.value)
                        return
                    } else if (secondKey.isInt() && secondKey.toInt() <= 0) {
                        takeIf { value.size > secondKey.toInt().absoluteValue }?.let {
                            tmpMap = value[secondKey.toInt().absoluteValue] as MutableMap<String, Any>
                        } ?: combineMaps(value, tmpMap)
                        leftKey = newKey(keyList, 3)
                    }
                    addEntry(AbstractMap.SimpleEntry<String, Any>(leftKey, entry.value), tmpMap)
                }
            }
        } else {
            when {
                keyList.size <= 3 && (secondKey.isInt() && secondKey.toInt() <= 0) -> {
                    jsonMap[key] = populateList(mutableListOf<Any>(), entry.value)
                    return
                }
                secondKey.isInt() && secondKey.toInt() <= 0 -> {
                    jsonMap[key] = combineMaps(mutableListOf<MutableMap<String, Any>>(), tmpMap)
                    leftKey = newKey(keyList, 3)
                }
                secondKey.isEmpty() -> {
                    jsonMap[key] = entry.value
                    return
                }
                else -> jsonMap[key] = tmpMap
            }
            addEntry(AbstractMap.SimpleEntry<String, Any>(leftKey, entry.value), tmpMap)
        }
    }

    private fun populateList(list: MutableList<*>, value: Any) = list.apply { asMutableListOfType<Any>()?.add(value) }

    private fun combineMaps(value: MutableList<*>, currentMap: MutableMap<String, Any>) =
        value.apply { asMutableListOfType<MutableMap<String, Any>>()?.add(currentMap) }

    private fun newKey(keyList: List<String>, start: Int) =
        keyList.subList(start).joinToString(jsonKeyDelimiter, jsonKeyDelimiter)

    private fun secondKey(keyList: List<String>) = takeIf { (keyList.size > 2) }?.let { keyList[2] } ?: ""
}