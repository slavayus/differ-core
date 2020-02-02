package com.differ.differcommit.tasks

import com.differ.differcommit.configuration.AppConfiguration
import com.differ.differcommit.exceptions.GitCredentialsNotProvidedException
import com.differ.differcommit.generator.IncrementLastFileProvider
import com.differ.differcommit.generator.IncrementVersionGenerator
import com.differ.differcommit.generator.VersionGenerator
import com.differ.differcommit.models.CommitProperties
import com.differ.differcommit.service.CommitService
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.springframework.context.annotation.AnnotationConfigApplicationContext


open class DifferCommit : DefaultTask() {

    @Internal
    lateinit var versionGenerator: VersionGenerator

    @TaskAction
    fun commit() {
        val context = AnnotationConfigApplicationContext(AppConfiguration::class.java)
        context
            .getBean(CommitProperties::class.java)
            .apply { initValuesFromProperties(this) }

        context.getBean(CommitService::class.java)
            .processNewApiVersion(versionGenerator)
    }

    private fun initValuesFromProperties(properties: CommitProperties) {
        with(properties) {
            project.properties[DIFFERCOMMIT_VERSIONS_HOME_DIR]?.toString()?.let { homeDir = it }
            project.properties[DIFFERCOMMIT_VERSIONS_COMMIT_MESSAGE]?.toString()?.let { commitMessage = it }
            project.properties[DIFFERCOMMIT_VERSIONS_TPM_DOC]?.toString()?.let { tmpDifferDoc = it }
            project.properties[DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED]?.toString()
                ?.let { pushRequired = it.toBoolean() }

            if (pushRequired) {
                username = project.properties[DIFFERCOMMIT_VERSIONS_GIT_USERNAME]?.toString()
                    ?: throw GitCredentialsNotProvidedException(
                        """There is no username for GIT account in properties. 
                          |Please provider property with key '$DIFFERCOMMIT_VERSIONS_GIT_USERNAME' and username as value. 
                          |Or you can set property '$DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED' to false if push is not required.""".trimMargin()
                    )

                password = project.properties[DIFFERCOMMIT_VERSIONS_GIT_PASSWORD]?.toString()
                    ?: throw GitCredentialsNotProvidedException(
                        """There is no password for GIT account in properties. 
                          |Please provider property with key '$DIFFERCOMMIT_VERSIONS_GIT_PASSWORD' and password as value. 
                          |Or you can set property '$DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED' to false if push is not required.""".trimMargin()
                    )
            }
        }
        if (!::versionGenerator.isInitialized) {
            versionGenerator = IncrementVersionGenerator(IncrementLastFileProvider())
        }
    }

    companion object {
        const val DIFFERCOMMIT_VERSIONS_HOME_DIR = "differcommit.versions.home-dir"
        const val DIFFERCOMMIT_VERSIONS_COMMIT_MESSAGE = "differcommit.versions.commit-message"
        const val DIFFERCOMMIT_VERSIONS_TPM_DOC = "differcommit.versions.tmp-doc"
        const val DIFFERCOMMIT_VERSIONS_GIT_USERNAME = "differcommit.versions.git.username"
        const val DIFFERCOMMIT_VERSIONS_GIT_PASSWORD = "differcommit.versions.git.password"
        const val DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED = "differcommit.versions.git.push-required"
    }
}