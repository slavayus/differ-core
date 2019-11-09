package com.differ.differcore.controllers

import com.differ.differcore.service.DiffService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping


@Controller
class DifferController(
    private val diffService: DiffService
) {

    @GetMapping(value = [DEFAULT_URL])
    fun getDiffer(model: Model): String {
        model["full"] = diffService.diff()
        model["left"] = diffService.difference.entriesOnlyOnLeft()
        model["right"] = diffService.difference.entriesOnlyOnRight()
        model["common"] = diffService.difference.entriesInCommon()
        return "template"
    }

    companion object {
        private const val DEFAULT_URL = "/v1/differ"
    }
}