package com.differ.differcore.configuration

import com.differ.differcore.service.SaveService
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

/**
 * Listening `ApplicationStartedEvent` from spring boot.
 *
 * Using [SaveService], initiates saving the API description to a temporary file.
 * The api description is saved for further processing by the gradle plugin.
 *
 * @author Vladislav Iusiumbeli
 * @since 1.0.0
 *
 * @constructor Is used for autowire required beans.
 *
 * @param saveService implementation of [SaveService] interface
 */
@Component
open class ApplicationStartedEventListener(
    private var saveService: SaveService
) : ApplicationListener<ApplicationStartedEvent> {

    /**
     * Handle an application started event.
     * @param event the event to respond to
     */
    override fun onApplicationEvent(event: ApplicationStartedEvent) = saveService.save()
}