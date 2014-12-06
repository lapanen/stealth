package org.lapanen.stealth.spring.rmi.server;

import org.lapanen.stealth.spring.rmi.api.StringService;
import org.springframework.context.annotation.Configuration;


public class StringServiceConfig extends AbstractServerConfig<StringService> {

    @Override
    public Class<StringService> serviceInterface() {
        return StringService.class;
    }

}
