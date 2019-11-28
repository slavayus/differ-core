package com.differ.differcore.utils

import com.google.common.collect.MapDifference
import freemarker.ext.beans.HashAdapter

class LeftRenderUtils {
    companion object {
        fun shouldRenderMethod(tag: String, content: HashAdapter?): Boolean {
            if (content == null) {
                return false
            }

            return (content["tags"] as List<*>)
                .stream()
                .filter { it != null }
                .map {
                    when (it) {
                        is MapDifference.ValueDifference<*> -> tag == it.leftValue()
                        is String -> it == tag
                        else -> false
                    }
                }
                .findAny()
                .get()
        }


        fun containsMethod(right: Map<String, Any?>, path: String, method: String): Boolean {
            val methods = ((right.values.first() as Map<*, *>)["paths"] as Map<*, *>)[path]
            return methods != null && (methods as Map<*, *>).containsKey(method)
        }

    }
}