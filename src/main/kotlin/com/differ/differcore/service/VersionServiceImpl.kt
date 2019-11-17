package com.differ.differcore.service

import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import javax.annotation.PostConstruct

@Service
class VersionServiceImpl(
    private val resourceLoader: ResourceLoader
) : VersionService {

    private lateinit var jversions: File

    @PostConstruct
    fun init() {
        jversions = resourceLoader.getResource("classpath:jversions").file
    }

    override fun getAllVersions(): List<String> {
        return jversions
            .listFiles()
            ?.map { it.name }
            ?.map { it.removeSuffix(".json") }
            ?: Collections.emptyList()
    }


}