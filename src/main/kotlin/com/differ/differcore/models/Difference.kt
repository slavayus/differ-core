package com.differ.differcore.models

/**
 * Holds differences between two versions.
 *
 * @see com.differ.differcore.service.DiffService.difference
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
data class Difference(
    /**
     * Union data from two versions
     */
    val full: Map<String, Any?>,

    /**
     * Entries representing only on left version
     */
    val onlyOnLeft: Map<String, Any?>,

    /**
     * Entries representing only on right version
     */
    val onlyOnRight: Map<String, Any?>
)