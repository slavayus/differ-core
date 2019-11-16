package com.differ.differcore.controllers

import com.differ.differcore.service.DiffService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import freemarker.ext.beans.BeansWrapper
import freemarker.template.Version


@Controller
class DifferController(
    private val diffService: DiffService
) {

    @GetMapping(value = [DEFAULT_URL])
    fun getDiffer(model: Model): String {
        val wrapper = BeansWrapper(Version(2, 3, 27))
        val statics = wrapper.staticModels
        model["statics"] = statics
        model["full"] = diffService.fullDiff()
        model["left"] = diffService.entriesOnlyOnLeft()
        model["right"] = diffService.entriesOnlyOnRight()
        model["common"] = diffService.entriesInCommon()
        return "template"
    }

    companion object {
        private const val DEFAULT_URL = "/v1/differ"
    }
}