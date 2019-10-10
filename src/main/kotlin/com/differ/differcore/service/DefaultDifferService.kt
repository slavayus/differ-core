package com.differ.differcore.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import springfox.documentation.spring.web.DocumentationCache
import java.io.File

@Service
internal open class DefaultDifferService(
    private val documentationCache: DocumentationCache,
    private val objectMapper: ObjectMapper
) : DifferService {
    private val defaultFileName = "differ-doc.json"

    override fun start() {
        objectMapper.writeValue(File(defaultFileName), documentationCache.all())
    }
}