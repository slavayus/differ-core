package com.differ.differcommit.tasks

import org.eclipse.jgit.api.Git
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.io.File

@Configuration
@ComponentScan("com.differ.differcommit.*")
@PropertySource("classpath:application.properties")
open class App(
    @Value("\${differ.json.versions.home-dir}")
    private val rawHomeDir: String
) {
    private val defaultFileName = "differ-doc.json"
    private val defaultCommitMessage = "Add new version of api"
    private val projectHome: File = File(System.getProperty("user.dir"))

    fun processNewApiVersion() {
        copyFile()
        makeNewCommit()
    }

    private fun copyFile() =
        File(defaultFileName).apply {
            copyTo(File(provideSaveFileDir() + defaultFileName), true)
//            delete()
        }

    private fun makeNewCommit() {
        val git = Git.open(projectHome)
        git.add().addFilepattern(rawHomeDir + File.separator + defaultFileName).call()
        git.commit().setMessage(defaultCommitMessage).call()
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

    private fun provideSaveFileDir() = projectHome.absolutePath + File.separator + rawHomeDir + File.separator
}