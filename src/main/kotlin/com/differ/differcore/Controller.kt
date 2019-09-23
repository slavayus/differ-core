package com.differ.differcore

import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping(value = ["/v2"])
    fun homeV2(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return "v2 ${simpleDateFormat.format(Date())}"
    }
}