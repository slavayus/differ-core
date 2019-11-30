package com.differ.differcore.service

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either
import com.differ.differcore.utils.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.MapDifference
import com.google.common.collect.Maps
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import kotlin.math.absoluteValue


@Service
class DiffServiceImpl(
    private val objectMapper: ObjectMapper,
    private val versionService: VersionService
) : DiffService {
    private val jsonKeySeparator = "."
    private val type = object : TypeReference<Map<String, Any>>() {}

    override fun difference(): Either<Difference> {
        val last = versionService.getLastVersionFile()
        val penultimate = versionService.getPenultimateVersionFile() ?: last

        return when {
            Objects.isNull(penultimate) -> Either.Error("No penultimate version files found ")
            Objects.isNull(last) -> Either.Error("No last version files found")
            else -> {
                val difference = difference(penultimate!!, last!!)
                Either.Success(Difference(fullDiff(difference), onlyOnLeft(difference), onlyOnRight(difference)))
            }
        }
    }

    override fun difference(penultimate: String, last: String): Either<Difference> {
        val penultimateFile = versionService.getVersionFile(penultimate)
        val lastFile = versionService.getVersionFile(last)

        return when {
            Objects.isNull(penultimateFile) -> Either.Error("No version $penultimate file found")
            Objects.isNull(lastFile) -> Either.Error("No version $last file found")
            else -> {
                val difference = difference(penultimateFile!!, lastFile!!)
                Either.Success(Difference(fullDiff(difference), onlyOnLeft(difference), onlyOnRight(difference)))
            }
        }
    }

    private fun difference(penultimate: File, last: File): MapDifference<String, Any?> {
        val leftFlatMap = flattenMap(penultimate, type)
        var rightFlatMap = flattenMap(last, type)

        if (rightFlatMap.isEmpty() && leftFlatMap.isNotEmpty()) {
            rightFlatMap = leftFlatMap
        }

        val difference = Maps.difference(leftFlatMap, rightFlatMap)

        writeDifferenceToFile(difference)
        return difference
    }

    private fun flattenMap(file: File, type: TypeReference<Map<String, Any>>) =
        file.let { objectMapper.readValue<Map<String, Any>>(it, type).flatten(jsonKeySeparator) }


    private fun writeDifferenceToFile(difference: MapDifference<String, Any?>) {
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


    private fun fullDiff(difference: MapDifference<String, Any?>): MutableMap<String, Any?> {
        return expandToMapObjects(
            mutableMapOf<String, Any?>().apply {
                putAll(difference.entriesOnlyOnLeft())
                putAll(difference.entriesOnlyOnRight())
                putAll(difference.entriesInCommon())
                putAll(difference.entriesDiffering())
            }.toSortedMap()
        )
    }

    private fun onlyOnLeft(difference: MapDifference<String, Any?>) =
        expandToMapObjects(difference.entriesOnlyOnLeft().toSortedMap())

    private fun onlyOnRight(difference: MapDifference<String, Any?>) =
        expandToMapObjects(difference.entriesOnlyOnRight().toSortedMap())

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