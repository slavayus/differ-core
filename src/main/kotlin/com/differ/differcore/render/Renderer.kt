package com.differ.differcore.render

import freemarker.ext.beans.HashAdapter

/**
 * Helpful functions for rendering differences.
 *
 * This is common functions for each render logic.
 *
 * @see LeftRenderer implementation of this interface for rendering an old version of API.
 * @see RightRenderer implementation of this interface for rendering a new version of API.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
abstract class Renderer {

    /**
     * Check if this map contains requested http method.
     *
     * @param map map to look for;
     * @param path path to look for method;
     * @param method method to find.
     *
     * @return `true` if path contains such http method, `false` otherwise.
     */
    fun containsMethod(map: Map<String, Any?>, path: String, method: String): Boolean {
        var methods: Any? = null
        if (map.values.isNotEmpty()) {
            methods = ((map.values.first() as Map<*, *>)["paths"] as Map<*, *>)[path]
        }
        return methods != null && (methods as Map<*, *>).containsKey(method)
    }

    /**
     * Check if specified http method contains requested parameter.
     *
     * @param map map to look for;
     * @param path path to look for method;
     * @param method method to look for parameter;
     * @param parameter parameter to find;
     * @param parameterIndex index from parameters array
     *
     * @return `true` if http method contains such parameter, `false` otherwise.
     */
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

    /**
     * Read value of the specified attribute.
     *
     * @param map map to look for;
     * @param attribute attribute to find value.
     *
     * @return Requested attribute value.
     */
    abstract fun attributeValue(map: Map<*, *>, attribute: String): Any?

    /**
     * Check whether to render method.
     *
     * @param content map to look for;
     * @param tag tag to find.
     *
     * @return `true` if need to render http method, `false` otherwise.
     */
    abstract fun shouldRenderMethod(tag: String, content: HashAdapter?): Boolean

    /**
     * Look up specified in url version in available versions.
     *
     * @param versions a list of available versions;
     * @param urlVersion version from url param.
     *
     * @return version from available list if there is such version, 'null' otherwise.
     */
    abstract fun versionSelected(versions: List<String>, urlVersion: String?): String?

    /**
     * Check if fully removed method from specified version.
     *
     * @param left map with left version;
     * @param right map with right version;
     * @param path path to look for method;
     * @param method method to find.
     *
     * @return `true` if method is fully removed from version, `false` otherwise.
     */
    abstract fun isFullyRemovedMethod(
        left: Map<String, Any?>,
        right: Map<String, Any?>,
        path: String,
        method: String
    ): Boolean

}