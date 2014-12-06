package org.lapanen.stealth.spring.rmi.client;

import org.lapanen.stealth.spring.rmi.AbstractRmiServiceClientConfig;
import org.springframework.context.annotation.Configuration;


public abstract class AbstractClientConfig<S> extends AbstractRmiServiceClientConfig<S> {
    @Override
    public String host() {
        return "localhost";
    }
}
