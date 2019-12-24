package com.differ.differcore.service

interface MapTransformerService {
    fun expandToMapObjects(unionData: MutableMap<String, Any>): MutableMap<String, Any?>
    fun flattenMap(mapToFlatten: Map<String, Any>): Map<String, Any?>
}