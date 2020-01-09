package com.differ.differcore.configuration

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

/**
 * Entire configuration of the Differ.
 *
 * Creates all the required beans for library life.
 * Links to the project only if the application is a web application.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
@ConditionalOnWebApplication
@Configuration
@ComponentScan(basePackages = ["com.differ.differcore"])
@PropertySource(value = ["classpath:application.properties"])
open class DifferConfiguration {

    @Bean
    open fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        return mapper
    }
}