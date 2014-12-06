package org.lapanen.stealth.spring.rmi;

public interface RmiServiceConfig<S> {

    Class<S> serviceInterface();

    String name();

    int registryPort();

    boolean usesSSL();

    boolean requiresSpringAuthentication();
}
