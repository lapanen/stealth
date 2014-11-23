package org.lapanen.stealth.si.subversion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 */
@Configuration
public class SubversionTcpComponents {
    @Bean
    public SubscribableChannel subversionCommitEventChannel() {
        return new PublishSubscribeChannel();
    }
}
