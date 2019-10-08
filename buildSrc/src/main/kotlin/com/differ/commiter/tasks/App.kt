package com.differ.commiter.tasks

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import java.io.File

@Configuration
@ComponentScan("com.differ.commiter.*")
@PropertySource("classpath:application.properties")
open class App(
    private val env: Environment
) {
    @Value("\${differ.json.versions.home-dir}")
    lateinit var rawHomeDir: String

    lateinit var projectHome: File

//    var differHomeDir: File =
//        File("$projectHome${File.separator}$rawHomeDir").apply { takeIf { !exists() }?.apply { mkdir() } }

    fun doSomething() {
        println(env["Yes"])
    }
}