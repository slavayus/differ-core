package com.differ.differcore.service

import com.differ.differcore.utils.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.MapDifference
import com.google.common.collect.Maps
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import javax.annotation.PostConstruct
import kotlin.math.absoluteValue


@Service
class DiffServiceImpl(
    private val objectMapper: ObjectMapper,
    private val versionService: VersionService
) : DiffService {
    private val jsonKeySeparator = "."
    private lateinit var difference: MapDifference<String, Any>

    @PostConstruct
    private fun difference() {
        val type = object : TypeReference<Map<String, Any>>() {}

        val leftFlatMap = flattenMap(versionService.getPenultimateVersionFile(), type)
        var rightFlatMap = flattenMap(versionService.getLastVersionFile(), type)

        if (rightFlatMap.isEmpty() && leftFlatMap.isNotEmpty()) {
            rightFlatMap = leftFlatMap
        }

        this.difference = Maps.difference(leftFlatMap, rightFlatMap)

        writeDifferenceToFile(difference)
    }

    private fun flattenMap(file: File?, type: TypeReference<Map<String, Any>>) =
        file?.let { objectMapper.readValue<Map<String, Any>>(it, type).flatten(jsonKeySeparator) }
            ?: mutableMapOf()


    private fun writeDifferenceToFile(difference: MapDifference<String, Any>) {
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
    }


    override fun fullDiff(): MutableMap<String, Any?> {
        return expandToMapObjects(
            mutableMapOf<String, Any>().apply {
                putAll(difference.entriesOnlyOnLeft())
                putAll(difference.entriesOnlyOnRight())
                putAll(difference.entriesInCommon())
                putAll(difference.entriesDiffering())
            }.toSortedMap()
        )
    }

    override fun entriesOnlyOnLeft() =
        expandToMapObjects(mutableMapOf<String, Any>().apply {
            putAll(difference.entriesOnlyOnLeft())
        }.toSortedMap())

    override fun entriesOnlyOnRight() =
        expandToMapObjects(mutableMapOf<String, Any>().apply {
            putAll(difference.entriesOnlyOnRight())
        }.toSortedMap())

    fun entriesInCommon() =
        expandToMapObjects(mutableMapOf<String, Any>().apply {
            putAll(difference.entriesInCommon())
        }.toSortedMap())

    override fun expandToMapObjects(unionData: MutableMap<String, Any>) =
        mutableMapOf<String, Any?>().apply {
            unionData.entries
                .forEach { addEntry(it, this) }
        }


    private fun addEntry(entry: Map.Entry<String, Any?>, jsonMap: MutableMap<String, Any?>) {
        val keyList = entry.key.split(jsonKeySeparator)
        val key = keyList[1]
        val value = jsonMap[key]
        val secondKey = secondKey(keyList)
        var tmpMap = mutableMapOf<String, Any?>()
        var leftKey = newKey(keyList, 2)
        if (value != null) {
            when (value) {
                is MutableMap<*, *> -> value.asMutableMapOfType<String, Any?>()?.let {
                    addEntry(AbstractMap.SimpleEntry<String, Any?>(newKey(keyList, 2), entry.value), it)
                }
                is MutableList<*> -> {
                    if (keyList.size <= 3 && (secondKey.isInt() && secondKey.toInt() <= 0)) {
                        populateList(value, entry.value)
                        return
                    } else if (secondKey.isInt() && secondKey.toInt() <= 0) {
                        takeIf { value.size > secondKey.toInt().absoluteValue }?.let {
                            tmpMap = value[secondKey.toInt().absoluteValue] as MutableMap<String, Any?>
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

    private fun populateList(list: MutableList<*>, value: Any?) = list.apply { asMutableListOfType<Any?>()?.add(value) }

    private fun combineMaps(value: MutableList<*>, currentMap: MutableMap<String, Any?>) =
        value.apply { asMutableListOfType<MutableMap<String, Any?>>()?.add(currentMap) }

    private fun newKey(keyList: List<String>, start: Int) =
        keyList.subList(start).joinToString(jsonKeySeparator, jsonKeySeparator)

    private fun secondKey(keyList: List<String>) = takeIf { (keyList.size > 2) }?.let { keyList[2] } ?: ""
}