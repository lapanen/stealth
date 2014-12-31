package org.lapanen.stealth.messaging.rabbitmq.config;

import org.lapanen.stealth.event.StealthEvent;
import org.lapanen.stealth.messaging.config.StealthMessagingOutbound;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.AmqpHeaders;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
@EnableIntegration
public class RabbitMqStealthMessagingOutbound extends RabbitMqStealthMessagingInfrastructure implements  StealthMessagingOutbound{

    /**
     * @return a handler that eventually encapsulates {@link #amqpHeaderSettingProcessor()}
     */
    @Bean
    protected MessageTransformingHandler amqpHeaderSettingTransformer() {
        final HeaderEnricher enricher = new HeaderEnricher();
        enricher.setMessageProcessor(amqpHeaderSettingProcessor());
        MessageTransformingHandler handler = new MessageTransformingHandler(enricher);
        handler.setOutputChannel(amqpOutChannel());
        stealthEventOutboundChannel().subscribe(handler);
        return handler;
    }

    /**
     * A processor that sets headers as follows:
     * <ul>
     * <li>{@value org.springframework.integration.amqp.AmqpHeaders#RECEIVED_ROUTING_KEY} to be the FQN of the message's payload</li>
     * <li>{@value org.springframework.integration.amqp.AmqpHeaders#RECEIVED_EXCHANGE} to be the return value of {@link #stealthExchange()}</li>
     * </ul>
     *
     * @return the processor
     */
    @Bean
    protected MessageProcessor amqpHeaderSettingProcessor() {
        return new MessageProcessor() {
            @Override
            public Object processMessage(final Message message) {
                final MessageBuilder builder = MessageBuilder.fromMessage(message);
                builder.setHeaderIfAbsent(AmqpHeaders.RECEIVED_ROUTING_KEY, message.getPayload().getClass().getCanonicalName());
                builder.setHeaderIfAbsent(AmqpHeaders.RECEIVED_EXCHANGE, stealthExchange());
                return builder.build();
            }
        };
    }

    @Bean
    protected SubscribableChannel amqpOutChannel() {
        return new DirectChannel();
    }

    @Bean
    protected AmqpOutboundEndpoint amqpOutboundEndpoint() {
        AmqpOutboundEndpoint outbound = new AmqpOutboundEndpoint(amqpTemplate());
        amqpOutChannel().subscribe(outbound);
        return outbound;
    }

    @Bean
    protected AmqpTemplate amqpTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Override
    @Bean
    public SubscribableChannel stealthEventOutboundChannel() {
        final DirectChannel channel = new DirectChannel();
        channel.setDatatypes(StealthEvent.class);
        return channel;
    }
}
