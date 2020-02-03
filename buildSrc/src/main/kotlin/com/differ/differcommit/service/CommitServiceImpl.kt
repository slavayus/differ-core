package com.differ.differcommit.service

import com.differ.differcommit.models.CommitProperties
import com.differ.differcommit.naming.generator.VersionGenerator
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.springframework.stereotype.Service
import java.io.File

@Service
class CommitServiceImpl(
    private val commitProperties: CommitProperties
) : CommitService {
    private lateinit var newApiVersion: String
    private lateinit var newApiVersionFileName: String

    override fun processNewApiVersion(versionGenerator: VersionGenerator) {
        newApiVersion = versionGenerator.generateVersionName(dversionsHomeDir())
        newApiVersionFileName = "$newApiVersion.json"
        copyFile()
        val git = openGit()
        makeNewCommit(git)
        pushCommit(git)
    }

    private fun openGit() = Git.open(commitProperties.projectHome)

    private fun copyFile() =
        File(commitProperties.tmpDifferDoc).apply {
            copyTo(File(dversionsHomeDir() + newApiVersionFileName), true)
            delete()
        }

    private fun makeNewCommit(git: Git) {
        git.reset().setMode(ResetCommand.ResetType.MIXED).call()
        git.add().addFilepattern(commitFileName()).call()
        git.commit().setMessage(commitMessage()).call()
    }

    private fun pushCommit(git: Git) {
        if (commitProperties.pushRequired) {
            git.push()
                .setCredentialsProvider(
                    UsernamePasswordCredentialsProvider(
                        commitProperties.username,
                        commitProperties.password
                    )
                )
                .call()
        }
    }

    private fun commitFileName() = commitProperties.homeDir + File.separator + newApiVersionFileName

    private fun commitMessage() = "${commitProperties.commitMessage}: $newApiVersion"

    private fun dversionsHomeDir() =
        commitProperties.projectHome.absolutePath + File.separator + commitProperties.homeDir + File.separator
}