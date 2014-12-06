package org.lapanen.stealth.spring.rmi.client;

import org.lapanen.stealth.spring.rmi.api.IntegerService;
import org.lapanen.stealth.spring.rmi.api.StringService;
import org.lapanen.stealth.spring.rmi.api.StringUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public IntegerService integerService() {
        return new IntegerServiceConfig().createClient();
    }
    @Bean
    public StringService stringService() {
        return new StringServiceConfig().createClient();
    }
    @Bean
    public StringUpdater stringUpdater() {
        return new StringUpdaterServiceConfig().createClient();
    }
}
