package com.differ.differcommit.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.springframework.context.annotation.AnnotationConfigApplicationContext


open class DifferCommit : DefaultTask() {
    @TaskAction
    fun commit() {
        AnnotationConfigApplicationContext(App::class.java)
            .getBean(App::class.java)
            .apply { setValues(this) }
            .processNewApiVersion()
    }

    private fun setValues(app: App) =
        with(app) {
            homeDir = project.properties[DIFFER_JSON_VERSIONS_HOME_DIR]?.toString()
            commitMessage = project.properties[DIFFER_JSON_VERSIONS_COMMIT_MESSAGE]?.toString()
        }

    companion object {
        const val DIFFER_JSON_VERSIONS_HOME_DIR = "differ.json.versions.home-dir"
        const val DIFFER_JSON_VERSIONS_COMMIT_MESSAGE = "differ.json.versions.commit-message"
        const val DIFFER_JSON_VERSIONS_TPM_DOC = "differ.json.versions.tmp-doc"
    }
}