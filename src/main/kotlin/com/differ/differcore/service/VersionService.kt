package com.differ.differcore.service

import java.io.File

/**
 * Service for working with existing API versions.
 *
 * @see VersionServiceImpl gets files with API description from file system.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
interface VersionService {

    /**
     * Looking for all available API versions.
     *
     * @return a list of available API versions
     */
    fun getAllVersions(): List<String>

    /**
     * Looking last version of API file.
     *
     * Last version file may be null due to different reasons. For example if there is no at least one API version.
     *
     * @return a file which represents last version of API.
     */
    fun getLastVersionFile(): File?

    /**
     * Looking penultimate version of API file.
     *
     * Penultimate version file may be null due to different reasons. For example there is no at least two API version.
     *
     * @return a file which represents penultimate version of API.
     */
    fun getPenultimateVersionFile(): File?

    /**
     * Looking specified version of API file.
     *
     * Specified version file may be null due to different reasons. For example there is no such API version.
     *
     * @param [version] API version file to look for.
     *
     * @return a file which represents specified version of API.
     */
    fun getVersionFile(version: String): File?
}