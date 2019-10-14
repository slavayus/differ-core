package com.differ.differcore.configuration

import com.differ.differcore.service.SaveService
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
open class ApplicationStartedEventListener(
    private var saveService: SaveService
) : ApplicationListener<ApplicationStartedEvent> {

    override fun onApplicationEvent(event: ApplicationStartedEvent) = saveService.start()
}