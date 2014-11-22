package org.lapanen.stealth.si.subversion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;

@Configuration
@EnableIntegration
public class SubversionComponents {

    @Bean
    public SubscribableChannel subversionLogChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public SubscribableChannel subversionBasePathChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public SubscribableChannel subversionPathMapper() {
        return new PublishSubscribeChannel();
    }

}
