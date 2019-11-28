package com.differ.differcore.service

import java.io.File

interface VersionService {
    fun getAllVersions(): List<String>
    fun getLastVersionFile(): File?
    fun getPenultimateVersionFile(): File?
    fun getVersionFile(version: String): File?
}