package com.differ.differcore.service

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either
import com.differ.differcore.utils.on
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.MapDifference
import com.google.common.collect.Maps
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

/**
 * Implementation of [DiffService] interface.
 *
 * Looking changes between versions with Google Gson library.
 * It uses 'com.google.common.collect.Maps.difference' for search.
 * Jsons are flattened first for a more accurate search. Jsons expand back after search.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 *
 * @constructor Is used for autowire required beans.
 *
 * @param objectMapper instance of jackson 'ObjectMapper' for deserialization json from file;
 * @param versionService implementation of [VersionService] interface;
 * @param mapTransformerService implementation of [MapTransformerService] interface;
 */
@Service
class DiffServiceImpl(
    private val objectMapper: ObjectMapper,
    private val versionService: VersionService,
    private val mapTransformerService: MapTransformerService
) : DiffService {

    /**
     * Json presentation in map format.
     */
    private val type = object : TypeReference<Map<String, Any>>() {}

    /**
     * Looks for changes in two specified versions.
     *
     * Searches [penultimate] and [last] versions in [VersionService].
     * If penultimate version is specified then it will be looked up [VersionService] else will be invoked [VersionService.getPenultimateVersionFile].
     * If last version is specified then it will be looked up [VersionService] else will be invoked [VersionService.getLastVersionFile].
     * If at least one file was not found then will be returned [Either.Error] with appropriate error message otherwise move on.
     *
     * After all the necessary files have been found, a search is made for changes in this json data. [Difference] object is the result of search.
     *
     * @param[penultimate] penultimate version of the API.
     * @param[last] last version of the API.
     *
     * @return The result of comparing two versions.
     * If all is fun then will be returned [Either.Success] else will be returned [Either.Error] with message or exception.
     */
    override fun difference(penultimate: String?, last: String?): Either<Difference> {
        val penultimateFile = provideVersionFile(penultimate) { versionService.getPenultimateVersionFile() }
        val lastFile = provideVersionFile(last) { versionService.getLastVersionFile() }

        return when {
            Objects.isNull(penultimateFile) && Objects.isNull(lastFile) -> Either.Error("No version files was found")
            Objects.isNull(penultimateFile) -> Either.Error("No version $penultimate file found")
            Objects.isNull(lastFile) -> Either.Error("No version $last file found")
            else -> {
                val difference = difference(penultimateFile!!, lastFile!!)
                Either.Success(Difference(fullDiff(difference), onlyOnLeft(difference), onlyOnRight(difference)))
            }
        }
    }

    private fun provideVersionFile(version: String?, orElse: () -> File?): File? =
        takeIf { version != null }?.let { versionService.getVersionFile(version!!) } ?: orElse()

    private fun difference(penultimate: File, last: File): MapDifference<String, Any?> {
        val leftFlatMap = mapTransformerService.flattenMap(readFileToMap(penultimate))
        val rightFlatMap = mapTransformerService.flattenMap(readFileToMap(last))

        val difference = Maps.difference(leftFlatMap, rightFlatMap)

        writeDifferenceToFile(difference)
        return difference
    }

    private fun readFileToMap(penultimate: File) = objectMapper.readValue<Map<String, Any>>(penultimate, type)

    private fun writeDifferenceToFile(difference: MapDifference<String, Any?>) {
        var outString = ""
        outString += "Entries only on left\n--------------------------\n"
        difference.entriesOnlyOnLeft().forEach { (key, value) -> outString += "$key: $value\n" }

        outString += "\n\nEntries only on right\n--------------------------\n"
        difference.entriesOnlyOnRight().forEach { (key, value) -> outString += "$key: $value\n" }

        outString += "\n\nEntries differing\n--------------------------\n"
        difference.entriesDiffering().forEach { (key, value) -> outString += "$key: $value\n" }

        outString += "\n\nEntries in common\n--------------------------\n"
        difference.entriesInCommon().forEach { (key, value) -> outString += "$key: $value\n" }
        val outFile = File("out-diff.txt")
        outFile.writeText(outString)
    }


    private fun fullDiff(difference: MapDifference<String, Any?>): MutableMap<String, Any?> {
        val flattenMap = on(sortedMapOf<String, Any?>()) {
            putAll(difference.entriesOnlyOnLeft())
            putAll(difference.entriesOnlyOnRight())
            putAll(difference.entriesInCommon())
            putAll(difference.entriesDiffering())
        }
        return mapTransformerService.expandToMapObjects(flattenMap)
    }

    private fun onlyOnLeft(difference: MapDifference<String, Any?>) =
        mapTransformerService.expandToMapObjects(difference.entriesOnlyOnLeft().toSortedMap())

    private fun onlyOnRight(difference: MapDifference<String, Any?>) =
        mapTransformerService.expandToMapObjects(difference.entriesOnlyOnRight().toSortedMap())
}