package com.differ.differcommit.service

import com.differ.differcommit.exceptions.GitHttpPasswordMissing
import com.differ.differcommit.exceptions.GitHttpUsernameMissing
import com.differ.differcommit.exceptions.GitSshRsaLocationMissing
import com.differ.differcommit.exceptions.GitSshRsaPasswordMissing
import com.differ.differcommit.models.CommitProperties
import com.differ.differcommit.naming.generator.VersionGenerator
import com.differ.differcommit.utils.requireNotNull
import com.jcraft.jsch.Session
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.transport.*
import org.eclipse.jgit.util.FS
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
                .setTransportConfigCallback { transport ->
                    when (transport) {
                        is SshTransport -> {
                            checkRequiredSshCredentials()
                            transport.sshSessionFactory = configureJschSessionFactory()
                        }
                        is HttpTransport -> {
                            checkRequiredHttpCredentials()
                            transport.credentialsProvider =
                                UsernamePasswordCredentialsProvider(
                                    commitProperties.username,
                                    commitProperties.password
                                )
                        }
                    }
                }
                .call()
        }
    }

    private fun configureJschSessionFactory() =
        object : JschConfigSessionFactory() {
            override fun configure(hc: OpenSshConfig.Host?, session: Session?) {
            }

            override fun getJSch(hc: OpenSshConfig.Host?, fs: FS?) =
                super.getJSch(hc, fs)
                    .apply {
                        removeAllIdentity()
                        addIdentity(commitProperties.sshRsaLocation, commitProperties.sshRsaPassword)
                    }
        }


    private fun checkRequiredSshCredentials() {
        with(commitProperties) {
            requireNotNull(sshRsaLocation) { GitSshRsaLocationMissing() }
            requireNotNull(sshRsaPassword) { GitSshRsaPasswordMissing() }
        }
    }

    private fun checkRequiredHttpCredentials() =
        with(commitProperties) {
            requireNotNull(username) { GitHttpUsernameMissing() }
            requireNotNull(password) { GitHttpPasswordMissing() }
        }

    private fun commitFileName() = commitProperties.homeDir + File.separator + newApiVersionFileName

    private fun commitMessage() = "${commitProperties.commitMessage}: $newApiVersion"

    private fun dversionsHomeDir() =
        commitProperties.projectHome.absolutePath + File.separator + commitProperties.homeDir + File.separator
}