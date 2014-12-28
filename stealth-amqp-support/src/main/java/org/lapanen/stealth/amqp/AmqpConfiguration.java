package org.lapanen.stealth.amqp;

import com.google.common.base.Optional;

public interface AmqpConfiguration {
    String amqpHostName();
    int amqpPort();
    Optional<String> amqpUsername();
    Optional<String> amqpPassword();
}
