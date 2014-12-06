package org.lapanen.stealth.spring.rmi.client;

import org.lapanen.stealth.spring.rmi.api.StringUpdater;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StringUpdaterServiceConfig extends AbstractClientConfig<StringUpdater> {

    @Override
    public Class<StringUpdater> serviceInterface() {
        return StringUpdater.class;
    }

}
