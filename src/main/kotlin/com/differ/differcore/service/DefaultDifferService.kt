package com.differ.differcore.service

import org.springframework.stereotype.Service
import springfox.documentation.spring.web.DocumentationCache

@Service
internal class DefaultDifferService(
    private val documentationCache: DocumentationCache
) : DifferService {
    override fun start() {
        println(documentationCache.all())
    }
}