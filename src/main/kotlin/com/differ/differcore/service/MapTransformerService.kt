package com.differ.differcore.service

/**
 * Service for transforming map.
 *
 * Service expand flattened map and vice versa.
 *
 * @see MapTransformerServiceImpl implementation of this interface.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
interface MapTransformerService {
    /**
     * Flatten json data in map format.
     *
     * For example if there is such flatten map entry `{ 1 : { 2 : 4 } }`
     * it should return `{ 1.2 : 4 }`.
     *
     * @param mapToFlatten expanded json data.
     *
     * @return Flattened expanded map.
     */
    fun flattenMap(mapToFlatten: Map<String, Any>): Map<String, Any?>

    /**
     * Expand flattened map to MutableMap.
     *
     * For example if there is such flatten map entry `{ 1.2 : 4 }`
     * it should return `{ 1 : { 2 : 4 } }`.
     *
     * @param flattenMap flattened json data.
     *
     * @return Expanded flattened map.
     */
    fun expandToMapObjects(flattenMap: Map<String, Any?>): MutableMap<String, Any?>
}