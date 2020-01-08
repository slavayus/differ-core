package com.differ.differcore.service

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import javax.annotation.PostConstruct

/**
 * Implementation of [VersionService] interface.
 *
 * Service uses `org.springframework.core.io.ResourceLoader` from `springframework` to get access to file system.
 * It look up all API version files in file system.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 *
 * @constructor Is used for autowire required beans.
 *
 * @param [resourceLoader] instance of springframework `ResourceLoader` for working with files.
 */
@Service
class VersionServiceImpl(
    private val resourceLoader: ResourceLoader
) : VersionService {

    /**
     * Location of all API version files.
     *
     * It should be a directory which contains API version files.
     */
    private lateinit var jversions: File

    @PostConstruct
    fun init() {
        jversions = resourceLoader.getResource(LOCATION).file
    }

    /**
     * Looking for all available API versions.
     *
     * It gets all files from `jversions` directory. collect only files name without suffix `.json`.
     *
     * @return a list of available API versions
     */
    override fun getAllVersions(): List<String> =
        jversions
            .listFiles()
            ?.map { it.name }
            ?.map { it.removeSuffix(".json") }
            ?: Collections.emptyList()

    /**
     * Looking last version of API file.
     *
     * Last version file may be null due to different reasons.
     * For example if there is no at least one API version.
     *
     * @return a file which represents last version of API.
     */
    override fun getLastVersionFile(): File? =
        jversions
            .listFiles()
            ?.max()

    /**
     * Looking penultimate version of API file.
     *
     * Penultimate version file may be null due to different reasons.
     * For example there is no at least two API version.
     *
     * @return a file which represents penultimate version of API.
     */
    override fun getPenultimateVersionFile(): File? {
        val sortedVersions = jversions.listFiles()?.sorted()
        return takeIf { sortedVersions != null && sortedVersions.size > 1 }?.let { sortedVersions!![sortedVersions.size - 2] }
    }

    /**
     * Looking specified version of API file.
     *
     * Specified version file may be null due to different reasons.
     * For example there is no such API version.
     *
     * @param [version] API version file to look for.
     *
     * @return a file which represents specified version of API.
     */
    override fun getVersionFile(version: String): File? =
        jversions.listFiles { _, name -> name == "$version.json" }.takeIf { it != null && it.isNotEmpty() }?.get(0)

    companion object {

        /**
         * Default location of directory with API version files.
         */
        private const val LOCATION = "classpath:jversions"
    }
}