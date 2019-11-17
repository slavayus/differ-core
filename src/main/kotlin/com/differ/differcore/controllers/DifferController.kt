package com.differ.differcore.controllers

import com.differ.differcore.service.DiffService
import com.differ.differcore.service.VersionService
import freemarker.template.Configuration
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import freemarker.template.DefaultObjectWrapperBuilder


@Controller
class DifferController(
    private val diffService: DiffService,
    private val versionService: VersionService
) {

    @GetMapping(value = [DEFAULT_URL])
    fun getDiffer(model: Model): String {
        model["statics"] = DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_29).build().staticModels
        model["full"] = diffService.fullDiff()
        model["left"] = diffService.entriesOnlyOnLeft()
        model["right"] = diffService.entriesOnlyOnRight()
        model["versions"] = versionService.getAllVersions()
//        model["common"] = diffService.entriesInCommon()
        return "template"
    }

    companion object {
        private const val DEFAULT_URL = "/v1/differ"
    }
}