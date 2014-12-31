package org.lapanen.stealth.messaging.config;

import org.lapanen.stealth.event.StealthEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;

@Configuration
@EnableIntegration
public interface StealthMessagingOutbound {

    @Bean
    public SubscribableChannel stealthEventOutboundChannel();
}
