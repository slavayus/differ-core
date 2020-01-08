package com.differ.differcore.render

import com.google.common.collect.MapDifference
import freemarker.ext.beans.HashAdapter
import org.springframework.stereotype.Component

/**
 * Helpful functions for rendering differences.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
@Component
open class RightRenderer : Renderer() {

    /**
     * Read value of the specified attribute.
     *
     * If value was modified then will be returned right value of the 'MapDifference.ValueDifference'
     *
     * @param map map to look for;
     * @param attribute attribute to find value.
     *
     * @return Requested attribute value.
     */
    override fun attributeValue(map: Map<*, *>, attribute: String) =
        with(map[attribute]) {
            when (this) {
                is MapDifference.ValueDifference<*> -> rightValue()
                is String -> this
                else -> null
            }
        }

    /**
     * Check if this map contains requested tag.
     *
     * @param content map to look for;
     * @param tag tag to find.
     *
     * @return `true` if this map contains requested tag, `false` otherwise.
     */
    override fun shouldRenderMethod(tag: String, content: HashAdapter?): Boolean {
        if (content == null || !content.containsKey("tags")) {
            return false
        }

        return (content["tags"] as List<*>)
            .stream()
            .filter { it != null }
            .anyMatch {
                when (it) {
                    is MapDifference.ValueDifference<*> -> tag == it.rightValue()
                    is String -> it == tag
                    else -> false
                }
            }
    }

    /**
     * Look up specified in url version in available versions.
     *
     * If requested version is listed in available versions list then it will be returned.
     * Else if there is no requested version then will be returned first version from available list
     * if list is not empty. In the end if all above returned false will be returned null.
     *
     * @param versions a list of available versions;
     * @param urlVersion version from url param.
     *
     * @return version from available list if there is such version, 'null' otherwise.
     */
    override fun versionSelected(versions: List<String>, urlVersion: String?): String? =
        when {
            versions.contains(urlVersion) -> urlVersion
            versions.isNotEmpty() -> versions.first()
            else -> null
        }

    /**
     * Check if fully removed method from specified version.
     *
     * This method is not implemented in this this renderer because method cannot be removed from right comlumn.
     *
     * @throws UnsupportedOperationException
     */
    override fun isFullyRemovedMethod(
        left: Map<String, Any?>,
        right: Map<String, Any?>,
        path: String,
        method: String
    ): Boolean {
        throw UnsupportedOperationException()
    }
}