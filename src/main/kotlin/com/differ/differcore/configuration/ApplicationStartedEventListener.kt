package com.differ.differcore.configuration

import com.differ.differcore.service.DifferService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
open class ApplicationStartedEventListener : ApplicationListener<ApplicationStartedEvent> {
    @Autowired
    lateinit var differService: DifferService

    override fun onApplicationEvent(event: ApplicationStartedEvent) = differService.start()
}