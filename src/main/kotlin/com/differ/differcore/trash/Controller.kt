package com.differ.differcore.trash

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*

@RestController
class Controller {
    @GetMapping(value = ["/"])
    fun home(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return simpleDateFormat.format(Date())
    }

    @PostMapping(value = ["/people"])
    fun people(@RequestBody people: People): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return simpleDateFormat.format(Date())
    }

    @PostMapping(value = ["/v2"])
    fun homeV2(): String {
//        val git = Git.open(File(System.getProperty("user.dir")))
//        val revWalk = RevWalk(git.repository)
//        val commit = git.log().add(git.repository.resolve(git.repository.branch)).call().first()
//
//        return TreeWalk.forPath(git.repository, "src/main/kotlin/com/differ/differcore/Controller.kt", commit.tree).use { treeWalk ->
//            val blobId = treeWalk.getObjectId(0)
//            git.repository.newObjectReader().use { objectReader ->
//                val objectLoader = objectReader.open(blobId)
//                String(objectLoader.bytes, StandardCharsets.UTF_8)
//            }
//        }

//        ​println("Having tree: ${commit.tree}")
/*        val treeWalk = TreeWalk(git.repository)
        treeWalk.addTree(commit.tree)
        treeWalk.isRecursive = true
        treeWalk.filter = PathFilter.create("src/main/kotlin/com/differ/differcore/Controller.kt")
        val objectId = treeWalk.getObjectId(0)
        val loader = git.repository.open(objectId)
        loader.copyTo(System.out)
        revWalk.dispose()
        return loader.bytes.toString()*/
        return "hellow"
    }
}