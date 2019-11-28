package com.differ.differcore.service

interface DiffService {
    fun fullDiff(): MutableMap<String, Any?>
    fun entriesOnlyOnLeft(): MutableMap<String, Any?>
    fun entriesOnlyOnRight(): MutableMap<String, Any?>
    fun expandToMapObjects(unionData: MutableMap<String, Any>): MutableMap<String, Any?>
    fun difference(penultimate: String, last: String)
}