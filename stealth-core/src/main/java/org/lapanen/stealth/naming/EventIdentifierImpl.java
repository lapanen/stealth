package org.lapanen.stealth.naming;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class EventIdentifierImpl implements EventIdentifier {

    private static String resolvedHostname;

    private final String name;

    private final UUID uuid;

    private final String hostname;

    private EventIdentifierImpl(final String name, final UUID uuid, final String hostname) {
        this.name = name;
        this.uuid = uuid;
        this.hostname = hostname;
    }

    public EventIdentifierImpl(final String name, final UUID uuid) {
        this(name, uuid, resolveHost());
    }

    public EventIdentifierImpl(final String name) {
        this(name, UUID.randomUUID(), resolveHost());
    }

    private static String resolveHost() {
        if (resolvedHostname == null) {
            try {
                resolvedHostname = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException e) {
                resolvedHostname = "UNRESOLVED.HOST";
            }
        }
        return resolvedHostname;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getHostName() {
        return hostname;
    }

    @Override
    public String toString() {
        return "EventIdentifier{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                ", hostname='" + hostname + '\'' +
                '}';
    }

}
