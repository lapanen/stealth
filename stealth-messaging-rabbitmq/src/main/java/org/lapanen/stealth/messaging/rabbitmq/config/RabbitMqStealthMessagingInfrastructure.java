package org.lapanen.stealth.messaging.rabbitmq.config;

import java.util.Map;

import org.lapanen.stealth.util.OptionalPreconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;

import com.google.common.base.Optional;

public class RabbitMqStealthMessagingInfrastructure {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqStealthMessagingInfrastructure.class);

    public static final int  RABBITMQ_DEFAULT_PORT = 666;
    public static final String RABBITMQ_DEFAULT_HOST = "localhost";
    public static final String DEFAULT_STEALTH_EXCHANGE = "x.stealth";

    /**
     * @return {@value #RABBITMQ_DEFAULT_HOST}
     */
    protected String host() {
        return RABBITMQ_DEFAULT_HOST;
    }

    /**
     * @return {@value #RABBITMQ_DEFAULT_PORT}
     */
    protected  int port() {
        return RABBITMQ_DEFAULT_PORT;
    }

    /**
     * @return {@link com.google.common.base.Optional#absent()}
     */
    protected  Optional<String> username() {
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
            log.warn("No username and/or password set for connection factory {}", factory);
        }
        return factory;
    }

    @Bean
    protected AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     *
     * @return the newly declared {@link org.springframework.amqp.core.TopicExchange}, by name {@link #stealthExchangeName()}.
     */
    @Bean
    protected Exchange stealthExchange() {
        final Exchange exchange = new TopicExchange(stealthExchangeName());
        amqpAdmin().declareExchange(exchange);
        return exchange;
    }

    protected Queue declareQueueAndBinding(final String name, final String routingKey) {
        return QueueExchangeBinding.Builder.toExchange(stealthExchange(), name).setRoutingKey(routingKey).build().declare(amqpAdmin()).getQueue();
    }

}
