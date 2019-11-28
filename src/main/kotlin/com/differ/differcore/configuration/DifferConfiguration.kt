package com.differ.differcore.configuration

import freemarker.template.DefaultObjectWrapperBuilder
import freemarker.template.TemplateHashModel
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@ConditionalOnWebApplication
@Configuration
@ComponentScan(basePackages = ["com.differ.differcore"])
@PropertySource(value = ["classpath:application.properties"])
open class DifferConfiguration {
    @Bean
    open fun freemarkerStatics(): TemplateHashModel =
        DefaultObjectWrapperBuilder(freemarker.template.Configuration.VERSION_2_3_29).build().staticModels
}