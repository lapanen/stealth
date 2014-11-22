package org.lapanen.stealth.si.subversion.annotation;

import org.springframework.integration.annotation.ServiceActivator;

/**
 */
@ServiceActivator(inputChannel = "subversionLogChannel")
public @interface SubversionLogEventConsumingServiceActivator {
    String outputChannel() default "";
}
