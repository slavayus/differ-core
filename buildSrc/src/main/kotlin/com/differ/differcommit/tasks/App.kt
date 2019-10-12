package com.differ.differcommit.tasks

import com.differ.differcommit.utils.isInt
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.io.File
import java.util.*

@Configuration
@ComponentScan("com.differ.differcommit.*")
@PropertySource("classpath:application.properties")
open class App(
    @Value("\${differ.json.versions.home-dir}")
    private val rawHomeDir: String,

    @Value("\${differ.json.versions.commit-message}")
    private val defaultCommitMessage: String,

    @Value("\${differ.json.versions.tmp-doc}")
    private val tmpDifferDoc: String
) {
    private var newApiVersionFileName: String
    private var newApiVersion: String
    private var projectHome: File = File(System.getProperty("user.dir"))

    init {
        newApiVersion = resolveNewApiVersion()
        newApiVersionFileName = "$newApiVersion.json"
    }

    fun processNewApiVersion() {
        copyFile()
        makeNewCommit()
    }

    private fun copyFile() =
        File(tmpDifferDoc).apply {
            copyTo(File(provideSaveFileDir() + newApiVersionFileName), true)
//            delete()
        }

    private fun makeNewCommit() {
        val git = Git.open(projectHome)
        git.add().addFilepattern(rawHomeDir + File.separator + newApiVersionFileName).call()
        git.commit().setMessage(resolveCommitMessage()).call()
        val pushCommand = git.push()
        pushCommand.setCredentialsProvider(
            UsernamePasswordCredentialsProvider(
                "slavayus",
                "slavik2354"
            )
        )
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

    private fun resolveCommitMessage() = "$defaultCommitMessage: $newApiVersion"

    private fun provideSaveFileDir() = projectHome.absolutePath + File.separator + rawHomeDir + File.separator

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