package com.differ.differcore.configuration

import com.differ.differcore.trash.Data
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@ConditionalOnWebApplication
@Configuration
@ComponentScan(basePackages = ["com.differ.differcore"])
open class DifferConfiguration {
    @Bean
    open fun data() = Data("YEE")
}