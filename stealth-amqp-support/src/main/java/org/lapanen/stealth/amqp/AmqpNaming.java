package org.lapanen.stealth.amqp;

import org.lapanen.stealth.event.StealthEvent;
import org.lapanen.stealth.naming.ComponentIdentifier;

public interface AmqpNaming {

    String createExchangeName(ComponentIdentifier sourceComponent);
    String createPermanentQueueName(ComponentIdentifier sourceComponent);
    <T extends StealthEvent> String createRoutingKeyForType(Class<T> type);
    <T extends StealthEvent> String createRoutingKeyForPackage(Class<T> type);
    <T extends StealthEvent> String createRoutingKeyForParentPackage(Class<T> type, int ancestry);

}
