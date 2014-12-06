package org.lapanen.stealth.spring.rmi.client;

import org.lapanen.stealth.spring.rmi.api.IntegerService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegerServiceConfig extends AbstractClientConfig<IntegerService> {

    @Override
    public Class<IntegerService> serviceInterface() {
        return IntegerService.class;
    }

}
