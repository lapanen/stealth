package org.lapanen.stealth;

import java.util.Collections;
import java.util.List;

import org.lapanen.stealth.event.StealthEvent;
import org.lapanen.stealth.maven.artifact.Artifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class DelegatingStealth<T extends StealthEvent> implements Stealth<T> {
    private static final Logger LOG = LoggerFactory.getLogger(DelegatingStealth.class);

    private final List<Stealth> handlers;

    public DelegatingStealth(final List<Stealth> handlers) {
        Preconditions.checkNotNull(handlers, "Handlers must not be null");
        Preconditions.checkArgument(!handlers.isEmpty(), "Handlers must not be an empty list");
        this.handlers = Collections.unmodifiableList(handlers);
    }

    @Override
    public void handleEvent(final T event) {
        for (final Stealth handler : handlers) {
            LOG.debug("Delegating to handler {}", handler);
            handler.handleEvent(event);
        }
    }
}
