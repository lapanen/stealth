package org.lapanen.stealth.spring.rmi.server;

import org.lapanen.stealth.spring.rmi.api.StringUpdater;
import org.springframework.context.annotation.Configuration;

public class StringUpdaterServiceConfig extends AbstractServerConfig<StringUpdater> {

    @Override
    public Class<StringUpdater> serviceInterface() {
        return StringUpdater.class;
    }

}
