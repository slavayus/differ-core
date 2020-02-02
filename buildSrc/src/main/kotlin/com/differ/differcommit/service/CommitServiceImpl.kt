package com.differ.differcommit.service

import com.differ.differcommit.models.CommitProperties
import com.differ.differcommit.utils.isInt
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.springframework.stereotype.Service
import java.io.File
import java.util.*

@Service
class CommitServiceImpl(
    private val commitProperties: CommitProperties
) : CommitService {
    private var newApiVersionFileName: String
    private var newApiVersion: String

    init {
        newApiVersion = resolveNewApiVersion()
        newApiVersionFileName = "$newApiVersion.json"
    }

    override fun processNewApiVersion() {
        copyFile()
        makeNewCommit()
    }

    private fun copyFile() =
        File(commitProperties.tmpDifferDoc).apply {
            copyTo(File(provideSaveFileDir() + newApiVersionFileName), true)
//            delete()
        }

    private fun makeNewCommit() {
        val git = Git.open(commitProperties.projectHome)
        git.reset().setMode(ResetCommand.ResetType.MIXED).call()
        git.add().addFilepattern(commitProperties.homeDir + File.separator + newApiVersionFileName).call()
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

    private fun commitMessage() = "${commitProperties.commitMessage}: $newApiVersion"

    private fun provideSaveFileDir() =
        commitProperties.projectHome.absolutePath + File.separator + commitProperties.homeDir + File.separator

    private fun resolveNewApiVersion() =
        Arrays.stream(File(provideSaveFileDir()).listFiles() ?: emptyArray())
            .map { it.name.split(".")[0] }
            .filter { it.isInt() }
            .map { it.toInt() }
            .max(Integer::compare)
            .map { it + 1 }
            .map { it.toString() }
            .map { it.padStart(4, '0') }
            .orElse("0001")
}