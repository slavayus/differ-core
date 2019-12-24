package com.differ.differcore.service

import com.differ.differcore.utils.*
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.absoluteValue

@Service
class MapTransformerServiceImpl : MapTransformerService {

    override fun flattenMap(mapToFlatten: Map<String, Any>): Map<String, Any?> {
        return mapToFlatten.flatten(JSON_KEY_SEPARATOR)
    }

    override fun expandToMapObjects(unionData: MutableMap<String, Any>) =
        mutableMapOf<String, Any?>().apply {
            unionData.entries
                .forEach { addEntry(it, this) }
        }


    private fun addEntry(entry: Map.Entry<String, Any?>, jsonMap: MutableMap<String, Any?>) {
        val keyList = entry.key.split(JSON_KEY_SEPARATOR)
        val key = keyList[1]
        val value = jsonMap[key]
        val secondKey = secondKey(keyList)
        var leftKey = newKey(keyList, 2)
        var tmpMap = mutableMapOf<String, Any?>()
        when {
            value is MutableMap<*, *> -> tmpMap = value.asMutableMapOfType()!!

            keyList.size <= 3 && secondKey.isNegativeNumber() -> {
                jsonMap[key] = populateList(if (value is MutableList<*>) value else mutableListOf<Any>(), entry.value)
                return
            }

            secondKey.isNegativeNumber() -> {
                jsonMap.putIfAbsent(key, mutableListOf(tmpMap))
                tmpMap = addToListIfAbsent(jsonMap[key] as MutableList<*>, secondKey)
                leftKey = newKey(keyList, 3)
            }

            secondKey.isEmpty() -> {
                jsonMap[key] = entry.value
                return
            }

            else -> jsonMap[key] = tmpMap
        }
        addEntry(AbstractMap.SimpleEntry(leftKey, entry.value), tmpMap)
    }

    private fun addToListIfAbsent(value: MutableList<*>, secondKey: String): MutableMap<String, Any?> {
        val result: MutableMap<String, Any?>?
        if (value.size > secondKey.toInt().absoluteValue) {
            result = value[secondKey.toInt().absoluteValue] as MutableMap<String, Any?>
        } else {
            result = mutableMapOf()
            populateList(value, result)
        }
        return result
    }

    private fun populateList(list: MutableList<*>, value: Any?) = list.apply { asMutableListOfType<Any?>()?.add(value) }

    private fun newKey(keyList: List<String>, start: Int) =
        keyList.subList(start).joinToString(JSON_KEY_SEPARATOR, JSON_KEY_SEPARATOR)

    private fun secondKey(keyList: List<String>) = takeIf { (keyList.size > 2) }?.let { keyList[2] } ?: ""

    companion object {
        private const val JSON_KEY_SEPARATOR = "."
    }
}