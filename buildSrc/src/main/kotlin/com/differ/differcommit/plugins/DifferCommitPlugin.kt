package com.differ.differcommit.plugins

import com.differ.differcommit.tasks.DifferCommit
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class DifferCommitPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        addPropertiesToProject(project)
        project.tasks.create("differcommit", DifferCommit::class.java)
    }

    private fun addPropertiesToProject(project: Project) {
        if (Files.exists(Paths.get("${project.rootDir}/src/main/resources/application.properties"))) {
            val localProperties = Properties()
            localProperties.load(FileInputStream("${project.rootDir}/src/main/resources/application.properties"))
//            localProperties.forEach { prop -> project.properties.put("asfd", "asf") }
            localProperties.forEach { prop -> println("${prop.key}, ${prop.value}") }
        }
    }
}