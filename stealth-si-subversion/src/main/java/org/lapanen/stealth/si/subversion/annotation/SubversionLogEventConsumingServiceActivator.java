package org.lapanen.stealth.si.subversion.annotation;

import org.lapanen.stealth.si.subversion.config.SubversionComponents;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.ServiceActivator;

@ServiceActivator(inputChannel = "subversionLogChannel")
@Import(SubversionComponents.class)
public @interface SubversionLogEventConsumingServiceActivator {
    String outputChannel() default "";
}
