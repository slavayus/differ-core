package com.differ.differcore.service

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either

interface DiffService {
    fun expandToMapObjects(unionData: MutableMap<String, Any>): MutableMap<String, Any?>
    fun difference(penultimate: String?, last: String?): Either<Difference>
}