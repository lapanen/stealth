package org.lapanen.stealth.spring.rmi;

import java.rmi.registry.Registry;

public abstract class AbstractRmiServiceConfig<S> implements RmiServiceConfig {
    public static final int DEFAULT_RMI_PORT = Registry.REGISTRY_PORT;

    public static final String SERVICE_NAME_POSTFIX = "Service";

    @Override
    public String name() {
        if (serviceInterface().getSimpleName().endsWith(SERVICE_NAME_POSTFIX)) {
            return serviceInterface().getSimpleName();
        }
        return serviceInterface().getSimpleName().concat(SERVICE_NAME_POSTFIX);
    }

    @Override
    public int registryPort() {
        return DEFAULT_RMI_PORT;
    }
    @Override
    public boolean usesSSL() {
        return false;
    }

    @Override
    public boolean requiresSpringAuthentication() {
        return false;
    }


}
