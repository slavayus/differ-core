package com.differ.differcommit.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.springframework.context.annotation.AnnotationConfigApplicationContext


open class DifferCommit : DefaultTask() {
    @TaskAction
    fun commit() {
        AnnotationConfigApplicationContext(App::class.java)
            .getBean(App::class.java)
            .processNewApiVersion()
    }
}