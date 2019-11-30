package com.differ.differcore.render

import com.google.common.collect.MapDifference
import freemarker.ext.beans.HashAdapter

abstract class Renderer {
    fun containsMethod(map: Map<String, Any?>, path: String, method: String): Boolean {
        var methods: Any? = null
        if (map.values.isNotEmpty()) {
            methods = ((map.values.first() as Map<*, *>)["paths"] as Map<*, *>)[path]
        }
        return methods != null && (methods as Map<*, *>).containsKey(method)
    }

    fun attributeValue(attribute: Map<*, *>, value: String) =
        with(attribute[value]) {
            when (this) {
                is MapDifference.ValueDifference<*> -> rightValue()
                else -> this
            }
        }

    abstract fun shouldRenderMethod(tag: String, content: HashAdapter?): Boolean

    abstract fun versionSelected(versions: List<String>, urlVersion: String?): String?
}