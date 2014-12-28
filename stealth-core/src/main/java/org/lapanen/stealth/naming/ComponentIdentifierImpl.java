package org.lapanen.stealth.naming;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public class ComponentIdentifierImpl implements ComponentIdentifier {

    private static String resolvedHostname;

    private final String name;

    private final String hostname;

    private ComponentIdentifierImpl(final String name, final String hostname) {
        this.name = name;
        this.hostname = hostname;
    }

    public ComponentIdentifierImpl(final String name) {
        this(name, resolveHost());
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
    public String getHostName() {
        return hostname;
    }

    @Override
    public String toString() {
        return "ComponentIdentifier{" +
                "name='" + name + '\'' +
                ", hostname='" + hostname + '\'' +
                '}';
    }

}
