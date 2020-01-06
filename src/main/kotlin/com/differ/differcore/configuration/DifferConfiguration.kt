package com.differ.differcore.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
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
open class DifferConfiguration