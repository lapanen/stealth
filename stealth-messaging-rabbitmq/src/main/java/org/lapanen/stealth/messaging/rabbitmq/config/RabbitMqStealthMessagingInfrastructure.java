package org.lapanen.stealth.messaging.rabbitmq.config;

import org.lapanen.stealth.messaging.config.StealthMessagingInfrastructure;
import org.lapanen.stealth.util.OptionalPreconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.AmqpHeaders;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.handler.MessageProcessor;
import org.springframework.integration.transformer.HeaderEnricher;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.google.common.base.Optional;

/**
 * Users that inherit this configuration should override non-{@link org.springframework.context.annotation.Bean} methods to set the actual values.
 */
@Configuration
@EnableIntegration
public class RabbitMqStealthMessagingInfrastructure extends StealthMessagingInfrastructure {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitMqStealthMessagingInfrastructure.class);

    public static final int RABBITMQ_DEFAULT_PORT = 666;
    public static final String RABBITMQ_DEFAULT_HOST = "localhost";
    public static final String DEFAULT_STEALTH_EXCHANGE = "x.stealth";

    @Bean
    protected AmqpInboundChannelAdapter amqpInboundChannelAdapter() {
        final AmqpInboundChannelAdapter inbound = new AmqpInboundChannelAdapter(amqpAbstractMessageListenerContainer());
        inbound.setOutputChannel(stealthEventInboundChannel());
        return inbound;
    }

    @Bean
    protected AbstractMessageListenerContainer amqpAbstractMessageListenerContainer() {
        return new SimpleMessageListenerContainer(connectionFactory());
    }

    /**
     * @return a {@link org.springframework.amqp.rabbit.connection.CachingConnectionFactory} with {@link #host()} and {@link #port()} set. {@link #username()}
     * and {@link #password()} are set, if both are {@link com.google.common.base.Optional#isPresent() present}.
     */
    @Bean
    protected ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(host(), port());
        try {
            factory.setUsername(OptionalPreconditions.checkOthersArePresent(username(), password()));
            factory.setPassword(OptionalPreconditions.checkOthersArePresent(password(), username()));
        } catch (IllegalStateException e) {
            LOG.warn("No username and/or password set for connection factory {}", factory);
        }
        return factory;
    }

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
     * <li>{@value org.springframework.integration.amqp.AmqpHeaders#RECEIVED_EXCHANGE} to be the return value of {@link #stealthExchangeName()}</li>
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
                builder.setHeaderIfAbsent(AmqpHeaders.RECEIVED_EXCHANGE, stealthExchangeName());
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

    /**
     * @return {@value #RABBITMQ_DEFAULT_HOST}
     */
    protected String host() {
        return RABBITMQ_DEFAULT_HOST;
    }

    /**
     * @return {@value #RABBITMQ_DEFAULT_PORT}
     */
    protected int port() {
        return RABBITMQ_DEFAULT_PORT;
    }

    /**
     * @return {@link com.google.common.base.Optional#absent()}
     */
    protected Optional<String> username() {
        return Optional.absent();
    }

    /**
     * @return {@link com.google.common.base.Optional#absent()}
     */
    protected Optional<String> password() {
        return Optional.absent();
    }

    /**
     * @return {@value #DEFAULT_STEALTH_EXCHANGE}
     */
    protected String stealthExchangeName() {
        return DEFAULT_STEALTH_EXCHANGE;
    }

}
