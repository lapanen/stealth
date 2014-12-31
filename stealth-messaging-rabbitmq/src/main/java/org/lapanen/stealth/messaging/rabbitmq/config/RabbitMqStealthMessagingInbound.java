package org.lapanen.stealth.messaging.rabbitmq.config;

import java.util.UUID;

import org.lapanen.stealth.event.StealthEvent;
import org.lapanen.stealth.messaging.config.StealthMessagingInbound;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.messaging.SubscribableChannel;

@Configuration
@EnableIntegration
public class RabbitMqStealthMessagingInbound extends RabbitMqStealthMessagingInfrastructure implements StealthMessagingInbound {

    @Override
    @Bean
    public SubscribableChannel stealthEventInboundChannel() {
        final PublishSubscribeChannel channel = new PublishSubscribeChannel();
        channel.setDatatypes(StealthEvent.class);
        return channel;

    }

    @Bean
    protected AmqpInboundChannelAdapter amqpInboundChannelAdapter() {
        final AmqpInboundChannelAdapter inbound = new AmqpInboundChannelAdapter(amqpAbstractMessageListenerContainer());
        inbound.setOutputChannel(stealthEventInboundChannel());
        return inbound;
    }

    @Bean
    protected AbstractMessageListenerContainer amqpAbstractMessageListenerContainer() {
        final SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory());
        for (final Queue queue : boundQueues()) {
            listenerContainer.addQueues(queue);
        }
        return listenerContainer;
    }

    @Bean
    protected Queue[] boundQueues() {
        Queue[] queues = new Queue[] { //
                declareQueueAndBinding("q." + UUID.randomUUID().toString(), "#") //
        };
        return queues;
    }

}
