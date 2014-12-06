package org.lapanen.stealth.spring.rmi.client;

import org.lapanen.stealth.spring.rmi.api.StringService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StringServiceConfig extends AbstractClientConfig<StringService> {

    @Override
    public Class<StringService> serviceInterface() {
        return StringService.class;
    }

}
