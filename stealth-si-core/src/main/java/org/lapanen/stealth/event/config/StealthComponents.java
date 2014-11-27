package org.lapanen.stealth.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.SubscribableChannel;

public class StealthComponents {

    @Bean public SubscribableChannel stealthEventChannel() {
        return new PublishSubscribeChannel();
    }

}
