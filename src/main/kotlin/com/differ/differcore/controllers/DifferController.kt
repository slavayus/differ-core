package com.differ.differcore.controllers

import com.differ.differcore.service.DiffService
import com.differ.differcore.service.VersionService
import freemarker.template.TemplateHashModel
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping(value = [DifferController.DEFAULT_URL])
class DifferController(
    private val diffService: DiffService,
    private val versionService: VersionService,
    private val templateHashModel: TemplateHashModel
) {

    @GetMapping
    fun getDiffer(model: Model): String {
        model["statics"] = templateHashModel
        model["full"] = diffService.fullDiff()
        model["left"] = diffService.entriesOnlyOnLeft()
        model["right"] = diffService.entriesOnlyOnRight()
        model["versions"] = versionService.getAllVersions()
        return "template"
    }

    @GetMapping(value = ["/version"])
    fun get(
        model: Model,
        @RequestParam(value = "left") left: String,
        @RequestParam(value = "right") right: String
    ): String {
        diffService.difference(left, right)
        model["statics"] = templateHashModel
        model["full"] = diffService.fullDiff()
        model["left"] = diffService.entriesOnlyOnLeft()
        model["right"] = diffService.entriesOnlyOnRight()
        return "apiBuilder"
    }

    companion object {
        const val DEFAULT_URL = "/v1/differ"
    }
}