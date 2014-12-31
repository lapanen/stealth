package org.lapanen.stealth.messaging.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;

public class QueueExchangeBinding {

    private final Exchange exchange;
    private final Queue queue;
    private final Binding binding;

    public QueueExchangeBinding(Exchange exchange, final Queue queue, final Binding binding) {
        this.exchange = exchange;
        this.queue = queue;
        this.binding = binding;
    }

    public QueueExchangeBinding declare(final AmqpAdmin admin) {
        admin.declareExchange(exchange);
        admin.declareQueue(queue);
        admin.declareBinding(binding);
        return this;
    }

    public Queue getQueue() {
        return queue;
    }

    public static class Builder {

        private final Map<String, Object> queueArguments;

        private final Map<String, Object> bindingArguments;

        private final Exchange exchange;

        private final String queueName;

        private boolean queueIsDurable = true;

        private boolean queueIsExclusive = false;

        private boolean queueIsAutoDelete = false;

        private Binding.DestinationType destinationType = Binding.DestinationType.QUEUE;

        private String routingKey = "#";

        private Builder(final Exchange exchange, final String queueName) {
            this.exchange = exchange;
            this.queueName = queueName;
            queueArguments = new HashMap<>();
            bindingArguments = new HashMap<>();
        }

        public static Builder toExchange(final Exchange exchange, final String queueName) {
            return new Builder(exchange, queueName);
        }

        public Builder setQueueArgument(final String key, final Object value) {
            queueArguments.put(key, value);
            return this;
        }

        public Builder setBindingArgument(final String key, final Object value) {
            bindingArguments.put(key, value);
            return this;
        }

        public Builder setRoutingKey(final String routingKey) {
            this.routingKey = routingKey;
            return this;
        }

        public QueueExchangeBinding build() {
            final Queue queue = new Queue(queueName, queueIsDurable, queueIsExclusive, queueIsAutoDelete, queueArguments);
            final Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchange.getName(), routingKey, bindingArguments);
            return new QueueExchangeBinding(exchange, queue, binding);
        }
    }

}
