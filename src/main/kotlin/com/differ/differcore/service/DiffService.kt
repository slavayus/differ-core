package com.differ.differcore.service

import com.differ.differcore.models.Difference

interface DiffService {
    fun expandToMapObjects(unionData: MutableMap<String, Any>): MutableMap<String, Any?>
    fun difference(penultimate: String, last: String): Difference
    fun difference(): Difference
}