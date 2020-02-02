package com.differ.differcommit.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ComponentScan("com.differ.differcommit.*")
@PropertySource("classpath:application.properties")
open class AppConfiguration