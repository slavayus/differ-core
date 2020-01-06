package com.differ.differcore.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.models.Swagger
import org.springframework.stereotype.Service
import springfox.documentation.spring.web.DocumentationCache
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper
import java.io.File

/**
 * Implementation of [SaveService] interface.
 *
 * Service uses `springfox.documentation.spring.web.DocumentationCache` from `springfox` to get full description of API.
 * It saves this description in file system.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 *
 * @constructor Is used for autowire required beans.
 *
 * @param documentationCache instance of `DocumentationCache` to get API description;
 * @param objectMapper instance of jackson `ObjectMapper` for serialization json to file;
 * @param mapper instance of `ServiceModelToSwagger2Mapper` to map `springfox` `Document` to `Swagger` object.
 */
@Service
open class SaveServiceImpl(
    private val documentationCache: DocumentationCache,
    private val objectMapper: ObjectMapper,
    private val mapper: ServiceModelToSwagger2Mapper
) : SaveService {

    private val defaultFileName = "differ-doc.json"

    /**
     * Mapping all `springfox` `Document` to `Swagger` object.
     */
    override fun save() {
        documentationCache.all()
            .entries
            .map { it.key to mapper.mapDocumentation(it.value) }
            .toMap()
            .let { saveMap(it) }
    }

    /**
     * Saving current API description in file system.
     */
    private fun saveMap(it: Map<String, Swagger>) =
        objectMapper.writeValue(File(defaultFileName), it)
}