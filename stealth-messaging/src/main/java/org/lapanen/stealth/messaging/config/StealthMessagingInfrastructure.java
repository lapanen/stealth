package org.lapanen.stealth.messaging.config;

import org.lapanen.stealth.event.StealthEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;

@Configuration
@EnableIntegration
public class StealthMessagingInfrastructure {

    @Bean
    public SubscribableChannel stealthEventInboundChannel() {
        final PublishSubscribeChannel channel = new PublishSubscribeChannel();
        channel.setDatatypes(StealthEvent.class);
        return channel;
    }

    @Bean
    public SubscribableChannel stealthEventOutboundChannel() {
        final PublishSubscribeChannel channel = new PublishSubscribeChannel();
        channel.setDatatypes(StealthEvent.class);
        return channel;
    }
}
