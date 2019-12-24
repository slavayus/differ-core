package com.differ.differcore.service

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either

interface DiffService {
    fun difference(penultimate: String?, last: String?): Either<Difference>
}