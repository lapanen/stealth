package org.lapanen.stealth.event;

import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.lapanen.stealth.naming.ComponentIdentifier;
import org.lapanen.stealth.naming.EventIdentifier;
import org.lapanen.stealth.naming.EventIdentifierImpl;

public abstract class AbstractStealthEvent implements StealthEvent {

    private final EventIdentifier eventIdentifier;

    private final ReadableInstant creationTime;

    protected AbstractStealthEvent(final EventIdentifier eventIdentifier) {
        this.eventIdentifier = eventIdentifier;
        creationTime = new Instant();
    }

    @Override
    public EventIdentifier getIdentifier() {
        return eventIdentifier;
    }

    @Override
    public ReadableInstant getCreationTime() {
        return creationTime;
    }
}
