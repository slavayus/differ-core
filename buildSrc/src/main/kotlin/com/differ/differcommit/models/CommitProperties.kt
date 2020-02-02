package com.differ.differcommit.models

import com.differ.differcommit.tasks.DifferCommit.Companion.DIFFERCOMMIT_VERSIONS_COMMIT_MESSAGE
import com.differ.differcommit.tasks.DifferCommit.Companion.DIFFERCOMMIT_VERSIONS_HOME_DIR
import com.differ.differcommit.tasks.DifferCommit.Companion.DIFFERCOMMIT_VERSIONS_TPM_DOC
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.io.File

@Configuration
@PropertySource("classpath:application.properties")
open class CommitProperties(
    @Value("\${$DIFFERCOMMIT_VERSIONS_HOME_DIR}")
    var homeDir: String,

    @Value("\${$DIFFERCOMMIT_VERSIONS_COMMIT_MESSAGE}")
    var commitMessage: String,

    @Value("\${$DIFFERCOMMIT_VERSIONS_TPM_DOC}")
    var tmpDifferDoc: String
) {
    var projectHome: File = File(System.getProperty("user.dir"))

    var pushRequired: Boolean = true

    lateinit var username: String

    lateinit var password: String
}