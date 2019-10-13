package com.differ.differcore.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import springfox.documentation.spring.web.DocumentationCache
import springfox.documentation.swagger2.web.Swagger2Controller
import java.io.File


@Service
internal open class DefaultSaveService(
    private val documentationCache: DocumentationCache,
    private val objectMapper: ObjectMapper,
    private val restTemplate: RestTemplate
) : SaveService {
    private val defaultFileName = "differ-doc.json"

    override fun start() =
        try {
            documentationCache.all()
                .entries
                .mapNotNull { (key, _) -> fetchSwaggerDocumentByGroup(key) }
                .toMap()
                .let { saveMap(it) }
        } catch (ex: RestClientException) {
            //todo log
        }

    private fun saveMap(it: Map<String, JsonNode>) =
        objectMapper.writeValue(File(defaultFileName), it)


    private fun fetchSwaggerDocumentByGroup(key: String) =
        restTemplate.getForEntity(requestPath(key), JsonNode::class.java)
            .takeIf { responseIsOk(it) }
            ?.body
            ?.let { key to it }

    private fun requestPath(group: String) = "http://localhost:8080/${Swagger2Controller.DEFAULT_URL}?group=$group"

    private fun responseIsOk(response: ResponseEntity<*>) =
        response.statusCode == HttpStatus.OK && response.body != null
}