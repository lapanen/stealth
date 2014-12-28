package org.lapanen.stealth.amqp;

import org.lapanen.stealth.event.StealthEvent;
import org.lapanen.stealth.maven.event.ArtifactBuildEvent;
import org.lapanen.stealth.naming.ComponentIdentifier;

import com.google.common.base.Optional;

public class AmqpSupport implements AmqpConfiguration, AmqpNaming {

    public static final String AMQP_DEFAULT_HOST = "localhost";
    public static final int AMQP_DEFAULT_PORT = 5147;

    private static final String NAME_SEPARATOR = ".";
    private static final String EXCHANGE_NAME_PREFIX = "x";
    private static final String QUEUE_NAME_PREFIX = "q";

    private static final String ROUTING_KEY_SEPARATOR = ".";
    private static final String ROUTING_KEY_WILDCARD = "#";

    /**
     * @return {@value #AMQP_DEFAULT_HOST}
     */
    @Override
    public String amqpHostName() {
        return AMQP_DEFAULT_HOST;
    }

    /**
     * @return {@value #AMQP_DEFAULT_PORT}
     */
    @Override
    public int amqpPort() {
        return AMQP_DEFAULT_PORT;
    }

    /**
     * @return {@link com.google.common.base.Optional#absent()}
     */
    @Override
    public Optional<String> amqpUsername() {
        return Optional.absent();
    }

    /**
     * @return {@link com.google.common.base.Optional#absent()}
     */
    @Override
    public Optional<String> amqpPassword() {
        return Optional.absent();
    }

    @Override
    public String createExchangeName(final ComponentIdentifier sourceComponent) {
        return EXCHANGE_NAME_PREFIX + NAME_SEPARATOR + sourceComponent.getName();
    }

    @Override
    public String createPermanentQueueName(final ComponentIdentifier sourceComponent) {
        return QUEUE_NAME_PREFIX + NAME_SEPARATOR + sourceComponent.getName() + NAME_SEPARATOR + sourceComponent.getHostName();
    }

    @Override
    public <T extends StealthEvent> String createRoutingKeyForType(final Class<T> type) {
        return type.getCanonicalName();
    }

    @Override
    public <T extends StealthEvent> String createRoutingKeyForPackage(final Class<T> type) {
        return type.getPackage().getName() + ROUTING_KEY_SEPARATOR + ROUTING_KEY_WILDCARD;
    }

    @Override
    public <T extends StealthEvent> String createRoutingKeyForParentPackage(final Class<T> type, final int ancestry) {
        final String[] packageParts = type.getPackage().getName().split("\\.");
        if (packageParts.length <= ancestry) {
            return ROUTING_KEY_WILDCARD;
        }
        final StringBuilder builder = new StringBuilder();
        for (int i = 0 ; i < packageParts.length - ancestry ; i++) {
            builder.append(packageParts[i]).append(ROUTING_KEY_SEPARATOR);
        }
        return builder.append(ROUTING_KEY_WILDCARD).toString();
    }
}
