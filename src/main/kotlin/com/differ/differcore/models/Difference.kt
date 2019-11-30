package com.differ.differcore.models

data class Difference(
    val full: Map<String, Any?>,
    val onlyOnLeft: Map<String, Any?>,
    val onlyOnRight: Map<String, Any?>
)