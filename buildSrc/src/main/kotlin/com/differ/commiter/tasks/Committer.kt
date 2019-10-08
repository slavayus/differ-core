package com.differ.commiter.tasks

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.TreeWalk
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.testfixtures.ProjectBuilder
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.io.File
import java.nio.charset.StandardCharsets


open class Committer : DefaultTask() {
    @InputDirectory
    var projectHome: File = File(System.getProperty("user.dir"))

    @TaskAction
    fun commit() {
        AnnotationConfigApplicationContext(App::class.java)
            .getBean(App::class.java)
            .run {
                projectHome = this@Committer.projectHome
                doSomething()
            }

        val git = Git.open(projectHome)
        val revWalk = RevWalk(git.repository)
        val commit = git.log().add(git.repository.resolve(git.repository.branch)).call().first()

        return TreeWalk.forPath(git.repository, "src/main/kotlin/com/differ/differcore/Controller.kt", commit.tree)
            .use { treeWalk ->
                val blobId = treeWalk.getObjectId(0)
                git.repository.newObjectReader().use { objectReader ->
                    val objectLoader = objectReader.open(blobId)
                    String(objectLoader.bytes, StandardCharsets.UTF_8)
                }
            }
    }
}