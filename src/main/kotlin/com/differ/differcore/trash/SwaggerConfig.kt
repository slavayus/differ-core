package com.differ.differcore.trash

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger.web.UiConfigurationBuilder


@Configuration
open class SwaggerConfig {

    @Bean
    open fun docket(apiInfo: ApiInfo): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("user-api")
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo)
            .select().paths(regex("/api/.*"))
            .build()
    }

    @Bean
    open fun docketHome(apiInfo: ApiInfo): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .groupName("home")
            .useDefaultResponseMessages(false)
            .apiInfo(apiInfo)
            .select().paths(regex("/.*"))
            .build()
    }

    @Bean
    open fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("User API")
            .description("API for fetching user related information")
            .version("1.0.0")
            .build()
    }

    @Bean
    open fun uiConfiguration(): UiConfiguration {
        return UiConfigurationBuilder.builder()
            .deepLinking(true)
            .validatorUrl(null)
            .build()
    }
}