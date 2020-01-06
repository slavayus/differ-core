package com.differ.differcore.service

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either

/**
 * Service for finding changes.
 *
 * Looks for changes in two specified versions.
 *
 * @see DiffServiceImpl implementation of this interface for finding changes.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
interface DiffService {

    /**
     * Looks for changes in two specified versions.
     *
     * Implementations should look up specified version and make comparison.
     *
     * @param[penultimate] penultimate version of the API.
     * @param[last] last version of the API.
     *
     * @return The result of comparing two versions. If all is fun then will be returned [Either.Success] else will be returned [Either.Error] with message or exception.
     */
    fun difference(penultimate: String?, last: String?): Either<Difference>
}