package com.differ.differcommit.service

import com.differ.differcommit.generator.VersionGenerator
import com.differ.differcommit.models.CommitProperties
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
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
        makeNewCommit()
    }

    private fun copyFile() =
        File(commitProperties.tmpDifferDoc).apply {
            copyTo(File(dversionsHomeDir() + newApiVersionFileName), true)
//            delete()
        }

    private fun makeNewCommit() {
        val git = Git.open(commitProperties.projectHome)
        git.reset().setMode(ResetCommand.ResetType.MIXED).call()
        git.add().addFilepattern(commitFileName()).call()
        git.commit().setMessage(commitMessage()).call()
        val pushCommand = git.push()
        // you can add more settings here if needed
//         pushCommand.call()
/*        val revWalk = RevWalk(git.repository)
        val commit = git.log().add(git.repository.resolve(git.repository.branch)).call().first()

        return TreeWalk.forPath(git.repository, "src/main/kotlin/com/differ/differcore/Controller.kt", commit.tree)
            .use { treeWalk ->
                val blobId = treeWalk.getObjectId(0)
                git.repository.newObjectReader().use { objectReader ->
                    val objectLoader = objectReader.open(blobId)
                    String(objectLoader.bytes, StandardCharsets.UTF_8)
                }
            }*/
    }

    private fun commitFileName() = commitProperties.homeDir + File.separator + newApiVersionFileName

    private fun commitMessage() = "${commitProperties.commitMessage}: $newApiVersion"

    private fun dversionsHomeDir() =
        commitProperties.projectHome.absolutePath + File.separator + commitProperties.homeDir + File.separator
}