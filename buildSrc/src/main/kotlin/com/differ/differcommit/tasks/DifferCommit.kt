package com.differ.differcommit.tasks

import com.differ.differcommit.configuration.AppConfiguration
import com.differ.differcommit.exceptions.*
import com.differ.differcommit.models.CommitProperties
import com.differ.differcommit.naming.generator.IncrementVersionGenerator
import com.differ.differcommit.naming.generator.VersionGenerator
import com.differ.differcommit.naming.provider.IncrementLastFileProvider
import com.differ.differcommit.service.CommitService
import com.differ.differcommit.utils.allIsNull
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.*


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
            project.properties[DIFFERCOMMIT_VERSIONS_GIT_COMMIT_MESSAGE]?.toString()?.let { commitMessage = it }
            project.properties[DIFFERCOMMIT_VERSIONS_TPM_DOC]?.toString()?.let { tmpDifferDoc = it }
            project.properties[DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED]?.toString()
                ?.let { pushRequired = it.toBoolean() }

            if (pushRequired) {
                username = project.properties[DIFFERCOMMIT_VERSIONS_GIT_HTTP_USERNAME]?.toString()
                password = project.properties[DIFFERCOMMIT_VERSIONS_GIT_HTTP_PASSWORD]?.toString()
                sshRsaLocation = project.properties[DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_LOCATION]?.toString()
                sshRsaPassword = project.properties[DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_PASSWORD]?.toString()
                when {
                    allIsNull(username, password, sshRsaLocation, sshRsaPassword) -> throw GitAllParametesMissing()
                    Objects.isNull(username) && Objects.nonNull(password) -> throw GitHttpUsernameMissing()
                    Objects.nonNull(username) && Objects.isNull(password) -> throw GitHttpPasswordMissing()
                    Objects.isNull(sshRsaLocation) && Objects.nonNull(sshRsaPassword) -> throw GitSshRsaLocationMissing()
                    Objects.nonNull(sshRsaLocation) && Objects.isNull(sshRsaPassword) -> throw GitSshRsaPasswordMissing()
                }
            }
        }
        if (!::versionGenerator.isInitialized) {
            versionGenerator = IncrementVersionGenerator(IncrementLastFileProvider())
        }
    }

    companion object {
        const val DIFFERCOMMIT_VERSIONS_HOME_DIR = "differcommit.versions.home-dir"
        const val DIFFERCOMMIT_VERSIONS_TPM_DOC = "differcommit.versions.tmp-doc"
        const val DIFFERCOMMIT_VERSIONS_GIT_COMMIT_MESSAGE = "differcommit.versions.git.commit-message"
        const val DIFFERCOMMIT_VERSIONS_GIT_PUSH_REQUIRED = "differcommit.versions.git.push-required"
        const val DIFFERCOMMIT_VERSIONS_GIT_HTTP_USERNAME = "differcommit.versions.git.http.username"
        const val DIFFERCOMMIT_VERSIONS_GIT_HTTP_PASSWORD = "differcommit.versions.git.http.password"
        const val DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_LOCATION = "differcommit.versions.git.ssh.rsa.location"
        const val DIFFERCOMMIT_VERSIONS_GIT_SSH_RSA_PASSWORD = "differcommit.versions.git.ssh.rsa.password"
    }
}