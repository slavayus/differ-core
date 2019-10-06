package com.differ.differcore.annotations

import com.differ.differcore.configuration.DifferConfiguration
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(value = AnnotationRetention.RUNTIME)
@Import(DifferConfiguration::class)
annotation class EnableDiffer