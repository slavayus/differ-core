package com.differ.differcore

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@EnableSwagger2
@Configuration
open class SwaggerConfig {

    @Bean
    open fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.differ.differcore"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(metaData())
    }

    private fun metaData(): ApiInfo {
        return ApiInfoBuilder()
            .title("ARchiteque API")
            .description("\"Spring Boot REST API for ARchiteque\"")
            .version("0.0.1")
            .license("Apache License Version 2.0")
            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
            .contact(Contact("ARchiteque LLC", "https://ar-chiteque.com/en", "info@ar-chiteque.com"))
            .build()
    }
}