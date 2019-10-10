package com.differ.differcommit.plugins

import com.differ.differcommit.tasks.DifferCommit
import org.gradle.api.Plugin
import org.gradle.api.Project

class DifferCommitPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.create("differcommit", DifferCommit::class.java)
    }
}