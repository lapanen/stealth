package org.lapanen.stealth.annotation;

import org.lapanen.stealth.event.config.StealthComponents;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.Publisher;

@Import(StealthComponents.class)
@Publisher(channel = "stealthEventChannel")
public @interface StealthPublisher {
    String componentName() default "";
}
