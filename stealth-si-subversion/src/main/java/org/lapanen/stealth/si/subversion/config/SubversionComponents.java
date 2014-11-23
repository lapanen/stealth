package org.lapanen.stealth.si.subversion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;
import org.tmatesoft.svn.core.SVNLogEntry;

@Configuration
@EnableIntegration
public class SubversionComponents {

    @Bean
    public SubscribableChannel subversionLogChannel() {
        final PublishSubscribeChannel channel = new PublishSubscribeChannel();
        channel.setDatatypes(SVNLogEntry.class);
        return channel;
    }

    @Bean
    public SubscribableChannel subversionBasePathChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public SubscribableChannel subversionPathMapperChannel() {
        return new PublishSubscribeChannel();
    }

}
