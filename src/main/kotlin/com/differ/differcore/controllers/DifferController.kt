package com.differ.differcore.controllers

import com.differ.differcore.service.DiffService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import java.util.*


@Controller
class DifferController(
    private val diffService: DiffService
) {

    @GetMapping(value = [DEFAULT_URL])
    fun getDiffer(model: Model): String {
//        diffService.diff()
        model["name"] = "Fremarker"
        val persons = ArrayList<List<*>>()
        persons.add(listOf("Alexander", "Petrov", 47))
        persons.add(listOf("Slava", "Petrov", 13))
        model["persons"] = persons
        return "template"
    }

    companion object {
        private const val DEFAULT_URL = "/v1/differ"
    }
}