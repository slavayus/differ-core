package com.differ.differcore.render

import freemarker.ext.beans.HashAdapter

abstract class Renderer {
    fun containsMethod(map: Map<String, Any?>, path: String, method: String): Boolean {
        var methods: Any? = null
        if (map.values.isNotEmpty()) {
            methods = ((map.values.first() as Map<*, *>)["paths"] as Map<*, *>)[path]
        }
        return methods != null && (methods as Map<*, *>).containsKey(method)
    }

    fun containsParameter(
        map: Map<String, Any?>,
        path: String,
        method: String,
        parameter: String,
        parameterIndex: Int
    ): Boolean {
        var methods: Any? = null
        if (map.values.isNotEmpty()) {
            methods = ((map.values.first() as Map<*, *>)["paths"] as Map<*, *>)[path]
        }
        if (methods != null && (methods as Map<*, *>)[method] != null) {
            return (((methods[method] as Map<*, *>)["parameters"] as List<*>)[parameterIndex] as Map<*, *>)[parameter] != null
        }
        return false
    }

    abstract fun attributeValue(attribute: Map<*, *>, value: String): Any?

    abstract fun shouldRenderMethod(tag: String, content: HashAdapter?): Boolean

    abstract fun versionSelected(versions: List<String>, urlVersion: String?): String?

    abstract fun isFullyRemovedMethod(
        left: Map<String, Any?>,
        right: Map<String, Any?>,
        path: String,
        method: String
    ): Boolean

}