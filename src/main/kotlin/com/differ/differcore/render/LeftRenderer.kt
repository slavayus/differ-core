package com.differ.differcore.render

import com.google.common.collect.MapDifference
import freemarker.ext.beans.HashAdapter
import org.springframework.stereotype.Component
import java.util.*

@Component
class LeftRenderer : Renderer() {
    override fun shouldRenderMethod(tag: String, content: HashAdapter?): Boolean {
        if (content == null) {
            return false
        }

        return (content["tags"] as List<*>)
            .stream()
            .filter { it != null }
            .anyMatch {
                when (it) {
                    is MapDifference.ValueDifference<*> -> tag == it.leftValue()
                    is String -> it == tag
                    else -> false
                }
            }
    }

    override fun versionSelected(versions: List<String>, urlVersion: String?): String? =
        if (Objects.isNull(urlVersion)) {
            if (versions.size > 1) {
                versions[1]
            } else {
                if (versions.isNotEmpty()) {
                    versions.first()
                } else {
                    null
                }
            }
        } else {
            versions.stream()
                .filter { it == urlVersion }
                .findFirst()
                .orElseGet { null }
        }

    override fun isFullyRemovedMethod(
        left: Map<String, Any?>,
        right: Map<String, Any?>,
        path: String,
        method: String
    ): Boolean {
        val rightContainsPath =
            right.values.isNotEmpty() && ((right.values.first() as Map<*, *>)["paths"] as Map<*, *>)[path] != null
        val leftContainsPath =
            left.values.isNotEmpty() && ((left.values.first() as Map<*, *>)["paths"] as Map<*, *>)[path] != null
        val leftContainMethod =
            leftContainsPath && (((left.values.first() as Map<*, *>)["paths"] as Map<*, *>)[path] as Map<*, *>)[method] != null
        return (!rightContainsPath && leftContainsPath) || leftContainMethod
    }

    override fun attributeValue(attribute: Map<*, *>, value: String) =
        with(attribute[value]) {
            when (this) {
                is MapDifference.ValueDifference<*> -> leftValue()
                else -> this
            }
        }

}