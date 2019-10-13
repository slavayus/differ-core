package com.differ.differcore.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.web.client.RestTemplate

@ConditionalOnWebApplication
@Configuration
@ComponentScan(basePackages = ["com.differ.differcore"])
@PropertySource(value = ["classpath:application.properties"])
open class DifferConfiguration {

    @Bean
    open fun restTemplate(builder: RestTemplateBuilder): RestTemplate =
        builder.rootUri("/").build()
}