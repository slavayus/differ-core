package com.differ.differcore.annotations

import com.differ.differcore.configuration.DifferConfiguration
import org.springframework.context.annotation.Import

/**
 * Indicates that Differ support should be enabled.
 *
 * This should be applied to a Spring java config and should have an accompanying `@Configuration` annotation.
 * This can be applied only on Spring web applications.
 *
 * Loads all required beans and configurations defined in [DifferConfiguration]
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(value = AnnotationRetention.RUNTIME)
@Import(DifferConfiguration::class)
annotation class EnableDiffer