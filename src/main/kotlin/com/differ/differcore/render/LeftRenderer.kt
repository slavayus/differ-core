package com.differ.differcore.render

import com.google.common.collect.MapDifference
import freemarker.ext.beans.HashAdapter
import org.springframework.stereotype.Component

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
}