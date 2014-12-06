package org.lapanen.stealth.spring.rmi.server;

import org.lapanen.stealth.spring.rmi.AbstractRmiServiceClientConfig;
import org.lapanen.stealth.spring.rmi.AbstractRmiServiceServerConfig;
import org.lapanen.stealth.spring.rmi.api.IntegerService;
import org.springframework.context.annotation.Configuration;


public class IntegerServiceConfig extends AbstractRmiServiceServerConfig<IntegerService> {

    @Override
    public Class<IntegerService> serviceInterface() {
        return IntegerService.class;
    }

}
