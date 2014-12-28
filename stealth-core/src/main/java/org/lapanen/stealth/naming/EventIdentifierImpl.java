package org.lapanen.stealth.naming;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class EventIdentifierImpl implements EventIdentifier {


    private final UUID uuid;

    private final ComponentIdentifier sourceComponent;

    public EventIdentifierImpl(final UUID uuid, final ComponentIdentifier sourceComponent) {
        this.uuid = uuid;
        this.sourceComponent = sourceComponent;
    }

    public EventIdentifierImpl(final ComponentIdentifier sourceComponent) {
        this(UUID.randomUUID(), sourceComponent);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public ComponentIdentifier getSourceComponent() {
        return sourceComponent;
    }

    @Override
    public String toString() {
        return "EventIdentifier{" +
                "uuid=" + uuid +
                '}';
    }

}
