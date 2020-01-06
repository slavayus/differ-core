package com.differ.differcore.controllers

import com.differ.differcore.controllers.DifferController.Companion.DEFAULT_URL
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

/**
 * Main controller of the application.
 *
 * By default all requests are mapping to the [DEFAULT_URL].
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 *
 * @constructor Is used for autowire required beans.
 *
 * @param diffService implementation of [DiffService] interface;
 * @param versionService implementation of [VersionService] interface;
 * @param leftRenderer bean with '@Qualifier("leftRenderer")', concrete instance of [Renderer] abstract class that renders left column;
 * @param rightRenderer bean with '@Qualifier("rightRenderer")', concrete instance of [Renderer] abstract class that renders right column.
 */
@Controller
@RequestMapping(value = [DifferController.DEFAULT_URL])
class DifferController(
    private val diffService: DiffService,
    private val versionService: VersionService,
    @Qualifier("leftRenderer") private val leftRenderer: Renderer,
    @Qualifier("rightRenderer") private val rightRenderer: Renderer
) {

    /**
     * Request for a full html page with version differences.
     *
     * Params can be null. Then it will search for suitable versions:
     *         if old version is null then it will search for penultimate version file;
     *         if new version is null then it will search for last version file.
     *
     * @param[old] an old version api for comparison;
     * @param[new] a new version api for comparison.
     *
     * @return Whole html page with differences in api versions.
     */
    @GetMapping
    fun getFullPage(
        model: Model,
        @RequestParam(value = "left", required = false) old: String?,
        @RequestParam(value = "right", required = false) new: String?
    ): String {
        val difference = diffService.difference(old, new)
        processDiffServiceResponse(difference, model)
        return "template"
    }


    /**
     * Request only diff part of the html page.
     *
     * It useful for ajax requests so as not to reload the page.
     * This params can't be null.
     *
     * @param[old] an old version api for comparison;
     * @param[new] a new version api for comparison.
     *
     * @return Only diff part of the html page.
     */
    @GetMapping(value = ["/version"])
    fun getOnlyDiff(
        model: Model,
        @RequestParam(value = "left") old: String,
        @RequestParam(value = "right") new: String
    ): String {
        val difference = diffService.difference(old, new)
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
        /**
         * Default url for all requests of this controller.
         */
        const val DEFAULT_URL = "/v1/differ"
    }
}