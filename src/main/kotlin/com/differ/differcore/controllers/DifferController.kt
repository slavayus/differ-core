package com.differ.differcore.controllers

import com.differ.differcore.models.Difference
import com.differ.differcore.models.Either
import com.differ.differcore.render.Renderer
import com.differ.differcore.service.DiffService
import com.differ.differcore.service.VersionService
import org.springframework.beans.factory.annotation.Qualifier
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
    @Qualifier("leftRenderer") private val leftRenderer: Renderer,
    @Qualifier("rightRenderer") private val rightRenderer: Renderer
) {


    @GetMapping
    fun getDiffer(model: Model): String {
        val difference = diffService.difference()
        processDiffServiceResponse(difference, model)
        return "template"
    }

    @GetMapping(value = ["/version"])
    fun get(
        model: Model,
        @RequestParam(value = "left") left: String,
        @RequestParam(value = "right") right: String
    ): String {
        val difference = diffService.difference(left, right)
        processDiffServiceResponse(difference, model)
        return "apiBuilder"
    }


    private fun processDiffServiceResponse(difference: Either<Difference>, model: Model) {
        when (difference) {
            is Either.Success -> {
                populateSuccessModel(model, difference.value)
            }
            is Either.Error -> {
                populateErrorModel(model, difference.message)
            }
        }
    }


    private fun populateSuccessModel(model: Model, difference: Difference) {
        model["full"] = difference.full
        model["left"] = difference.onlyOnLeft
        model["right"] = difference.onlyOnRight
        model["versions"] = versionService.getAllVersions()
        model["leftRenderer"] = leftRenderer
        model["rightRenderer"] = rightRenderer
    }

    private fun populateErrorModel(model: Model, message: String) {
        model["errorMessage"] = message
    }

    companion object {
        const val DEFAULT_URL = "/v1/differ"
    }
}